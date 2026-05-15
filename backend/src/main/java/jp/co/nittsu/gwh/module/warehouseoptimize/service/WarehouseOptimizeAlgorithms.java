package jp.co.nittsu.gwh.module.warehouseoptimize.service;

import jp.co.nittsu.gwh.module.warehouseoptimize.dto.WarehouseOptimizeModels;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

final class WarehouseOptimizeAlgorithms {

    private WarehouseOptimizeAlgorithms() {}

    static List<WarehouseOptimizeModels.SlottingAssignment> assignProducts(
            List<WarehouseOptimizeModels.ProductRecord> products,
            List<WarehouseOptimizeModels.WarehouseLayoutLocation> locations,
            Long profileId,
            String customerCode,
            String algorithm) {

        List<WarehouseOptimizeModels.ProductRecord> workingProducts = new ArrayList<>(products);
        workingProducts.sort(buildProductComparator(algorithm));

        List<WarehouseOptimizeModels.WarehouseLayoutLocation> candidateLocations = new ArrayList<>(locations);
        candidateLocations.sort(buildLocationComparator(algorithm));

        int limit = Math.min(workingProducts.size(), candidateLocations.size());
        List<WarehouseOptimizeModels.SlottingAssignment> assignments = new ArrayList<>();
        for (int index = 0; index < limit; index++) {
            WarehouseOptimizeModels.ProductRecord product = workingProducts.get(index);
            WarehouseOptimizeModels.WarehouseLayoutLocation location = candidateLocations.get(index);

            WarehouseOptimizeModels.SlottingAssignment assignment = new WarehouseOptimizeModels.SlottingAssignment();
            assignment.setProfileId(profileId);
            assignment.setCustomerCode(customerCode);
            assignment.setLocation(location.getLocation());
            assignment.setZone(location.getZone());
            assignment.setProductSku(product.getSku());
            assignment.setProductName(product.getName());
            assignment.setProductCategory(product.getCategory());
            assignment.setVelocityClass(safeVelocity(product.getVelocityClass()));
            assignment.setDemandFrequency(zeroIfNull(product.getDemandFrequency()));
            BigDecimal score = calculateAssignmentScore(product, location, algorithm);
            assignment.setAssignmentScore(score);
            assignment.setAssignmentReason(buildAssignmentReason(product, location, algorithm));
            assignments.add(assignment);
        }

        return assignments;
    }

    static WarehouseOptimizeModels.RouteOptimizationResult optimizeRoute(
            Long orderId,
            WarehouseOptimizeModels.RouteNode startPoint,
            List<WarehouseOptimizeModels.RouteNode> stops) {

        WarehouseOptimizeModels.RouteOptimizationResult result = new WarehouseOptimizeModels.RouteOptimizationResult();
        result.setOrderId(orderId);
        result.setStartPoint(startPoint);

        if (stops == null || stops.isEmpty()) {
            WarehouseOptimizeModels.RouteResult empty = new WarehouseOptimizeModels.RouteResult();
            empty.setMethod("Empty");
            empty.setDistance(BigDecimal.ZERO);
            result.setOriginal(empty);
            result.setBest(empty);
            return result;
        }

        Map<String, WarehouseOptimizeModels.RouteNode> stopMap = new HashMap<>();
        List<String> names = new ArrayList<>();
        for (WarehouseOptimizeModels.RouteNode stop : stops) {
            if (stop != null && stop.getName() != null && stopMap.putIfAbsent(stop.getName(), stop) == null) {
                names.add(stop.getName());
            }
        }

        WarehouseOptimizeModels.RouteResult original = toResult("Original", names, stopMap, startPoint);
        result.setOriginal(original);

        List<RouteCandidate> candidates = new ArrayList<>();
        candidates.add(new RouteCandidate("Nearest Neighbor", nearestNeighbor(names, stopMap, startPoint)));
        candidates.add(new RouteCandidate("2-Opt", twoOpt(names, stopMap, startPoint)));
        candidates.add(new RouteCandidate("Greedy Cluster", greedyCluster(names, stopMap, startPoint)));
        if (names.size() <= 9) {
            candidates.add(new RouteCandidate("Exact TSP", exactTsp(names, stopMap, startPoint)));
        }

        BigDecimal originalDistance = original.getDistance() == null ? BigDecimal.ZERO : original.getDistance();
        List<WarehouseOptimizeModels.RouteResult> optimized = new ArrayList<>();
        for (RouteCandidate candidate : candidates) {
            WarehouseOptimizeModels.RouteResult routeResult = toResult(candidate.method, candidate.route, stopMap, startPoint);
            routeResult.setPercent(calculateImprovement(originalDistance, routeResult.getDistance()));
            optimized.add(routeResult);
        }

        optimized.sort(Comparator.comparing(WarehouseOptimizeModels.RouteResult::getDistance));
        result.setOptimized(optimized);
        result.setComparison(new ArrayList<>(optimized));
        result.setBest(optimized.get(0));
        return result;
    }

    private static Comparator<WarehouseOptimizeModels.ProductRecord> buildProductComparator(String algorithm) {
        return (left, right) -> {
            String mode = algorithm == null ? "combined" : algorithm.toLowerCase();
            if ("abc".equals(mode) || "golden-zone".equals(mode)) {
                int velocityCompare = velocityRank(safeVelocity(left.getVelocityClass())) -
                        velocityRank(safeVelocity(right.getVelocityClass()));
                if (velocityCompare != 0) {
                    return velocityCompare;
                }
            }
            return zeroIfNull(right.getDemandFrequency()).compareTo(zeroIfNull(left.getDemandFrequency()));
        };
    }

    private static Comparator<WarehouseOptimizeModels.WarehouseLayoutLocation> buildLocationComparator(String algorithm) {
        return (left, right) -> {
            BigDecimal leftScore = locationScore(left, algorithm);
            BigDecimal rightScore = locationScore(right, algorithm);
            return rightScore.compareTo(leftScore);
        };
    }

    private static BigDecimal locationScore(WarehouseOptimizeModels.WarehouseLayoutLocation location, String algorithm) {
        BigDecimal score = BigDecimal.ZERO;
        if (location == null) {
            return score;
        }

        if ("HIGH_RACK".equalsIgnoreCase(location.getType())) {
            score = score.add(new BigDecimal("30"));
        } else if ("SHELF".equalsIgnoreCase(location.getType())) {
            score = score.add(new BigDecimal("25"));
        } else {
            score = score.add(new BigDecimal("10"));
        }

        int level = location.getLevel() == null ? 1 : location.getLevel();
        if ("golden-zone".equalsIgnoreCase(algorithm)) {
            score = score.add(new BigDecimal(Math.max(0, 25 - Math.abs(level - 2) * 8)));
        } else {
            score = score.add(new BigDecimal(Math.max(0, 18 - Math.abs(level - 1) * 4)));
        }

        if (location.getSlottedClass() != null && !location.getSlottedClass().isEmpty()) {
            score = score.add(new BigDecimal("5"));
        }

        return score;
    }

    private static BigDecimal calculateAssignmentScore(
            WarehouseOptimizeModels.ProductRecord product,
            WarehouseOptimizeModels.WarehouseLayoutLocation location,
            String algorithm) {
        BigDecimal demand = zeroIfNull(product.getDemandFrequency());
        BigDecimal score = demand.multiply(new BigDecimal("10")).add(locationScore(location, algorithm));
        int velocityRank = velocityRank(safeVelocity(product.getVelocityClass()));
        return score.subtract(new BigDecimal(velocityRank * 3)).setScale(4, RoundingMode.HALF_UP);
    }

    private static String buildAssignmentReason(
            WarehouseOptimizeModels.ProductRecord product,
            WarehouseOptimizeModels.WarehouseLayoutLocation location,
            String algorithm) {
        StringBuilder reason = new StringBuilder();
        reason.append(safeVelocity(product.getVelocityClass())).append("-class item");
        reason.append(" placed in ").append(location.getType());
        if ("golden-zone".equalsIgnoreCase(algorithm)) {
            reason.append(" with ergonomic priority");
        } else if ("abc".equalsIgnoreCase(algorithm)) {
            reason.append(" using ABC ranking");
        } else {
            reason.append(" using combined demand and level score");
        }
        return reason.toString();
    }

    private static WarehouseOptimizeModels.RouteResult toResult(
            String method,
            List<String> route,
            Map<String, WarehouseOptimizeModels.RouteNode> stopMap,
            WarehouseOptimizeModels.RouteNode startPoint) {
        WarehouseOptimizeModels.RouteResult result = new WarehouseOptimizeModels.RouteResult();
        result.setMethod(method);
        result.setRoute(route);
        result.setDistance(totalDistance(route, stopMap, startPoint));
        return result;
    }

    private static List<String> nearestNeighbor(
            List<String> names,
            Map<String, WarehouseOptimizeModels.RouteNode> stopMap,
            WarehouseOptimizeModels.RouteNode startPoint) {
        Set<String> remaining = new HashSet<>(names);
        List<String> route = new ArrayList<>();
        WarehouseOptimizeModels.RouteNode current = startPoint;

        while (!remaining.isEmpty()) {
            String nearestName = null;
            double nearestDistance = Double.MAX_VALUE;
            for (String candidate : remaining) {
                double distance = distance(current, stopMap.get(candidate));
                if (distance < nearestDistance) {
                    nearestDistance = distance;
                    nearestName = candidate;
                }
            }
            route.add(nearestName);
            current = stopMap.get(nearestName);
            remaining.remove(nearestName);
        }

        return route;
    }

    private static List<String> twoOpt(
            List<String> names,
            Map<String, WarehouseOptimizeModels.RouteNode> stopMap,
            WarehouseOptimizeModels.RouteNode startPoint) {
        List<String> route = nearestNeighbor(names, stopMap, startPoint);
        boolean improved = true;
        while (improved) {
            improved = false;
            for (int i = 0; i < route.size() - 1; i++) {
                for (int j = i + 1; j < route.size(); j++) {
                    List<String> candidate = new ArrayList<>(route);
                    Collections.reverse(candidate.subList(i, j + 1));
                    if (totalDistance(candidate, stopMap, startPoint)
                            .compareTo(totalDistance(route, stopMap, startPoint)) < 0) {
                        route = candidate;
                        improved = true;
                    }
                }
            }
        }
        return route;
    }

    private static List<String> greedyCluster(
            List<String> names,
            Map<String, WarehouseOptimizeModels.RouteNode> stopMap,
            WarehouseOptimizeModels.RouteNode startPoint) {
        List<String> route = new ArrayList<>(names);
        route.sort((left, right) -> {
            WarehouseOptimizeModels.RouteNode leftNode = stopMap.get(left);
            WarehouseOptimizeModels.RouteNode rightNode = stopMap.get(right);
            int aisleCompare = compareNullable(leftNode.getX(), rightNode.getX());
            if (aisleCompare != 0) {
                return aisleCompare;
            }
            return compareNullable(leftNode.getY(), rightNode.getY());
        });

        List<String> reordered = new ArrayList<>();
        boolean reverse = false;
        int index = 0;
        while (index < route.size()) {
            BigDecimal currentX = stopMap.get(route.get(index)).getX();
            List<String> sameAisle = new ArrayList<>();
            while (index < route.size() && compareNullable(currentX, stopMap.get(route.get(index)).getX()) == 0) {
                sameAisle.add(route.get(index));
                index++;
            }
            sameAisle.sort((left, right) -> compareNullable(stopMap.get(left).getY(), stopMap.get(right).getY()));
            if (reverse) {
                Collections.reverse(sameAisle);
            }
            reordered.addAll(sameAisle);
            reverse = !reverse;
        }
        return reordered;
    }

    private static List<String> exactTsp(
            List<String> names,
            Map<String, WarehouseOptimizeModels.RouteNode> stopMap,
            WarehouseOptimizeModels.RouteNode startPoint) {
        List<String> bestRoute = new ArrayList<>(names);
        BigDecimal bestDistance = totalDistance(bestRoute, stopMap, startPoint);
        permute(names, 0, stopMap, startPoint, bestRoute, bestDistance);
        return bestExactRoute;
    }

    private static List<String> bestExactRoute = new ArrayList<>();
    private static BigDecimal bestExactDistance = null;

    private static void permute(
            List<String> names,
            int index,
            Map<String, WarehouseOptimizeModels.RouteNode> stopMap,
            WarehouseOptimizeModels.RouteNode startPoint,
            List<String> initialBestRoute,
            BigDecimal initialBestDistance) {
        if (index == 0) {
            bestExactRoute = new ArrayList<>(initialBestRoute);
            bestExactDistance = initialBestDistance;
        }
        if (index == names.size()) {
            BigDecimal candidateDistance = totalDistance(names, stopMap, startPoint);
            if (candidateDistance.compareTo(bestExactDistance) < 0) {
                bestExactDistance = candidateDistance;
                bestExactRoute = new ArrayList<>(names);
            }
            return;
        }

        for (int i = index; i < names.size(); i++) {
            Collections.swap(names, index, i);
            permute(names, index + 1, stopMap, startPoint, initialBestRoute, initialBestDistance);
            Collections.swap(names, index, i);
        }
    }

    private static BigDecimal totalDistance(
            List<String> route,
            Map<String, WarehouseOptimizeModels.RouteNode> stopMap,
            WarehouseOptimizeModels.RouteNode startPoint) {
        double total = 0;
        WarehouseOptimizeModels.RouteNode current = startPoint;
        for (String stopName : route) {
            WarehouseOptimizeModels.RouteNode next = stopMap.get(stopName);
            total += distance(current, next);
            current = next;
        }
        return new BigDecimal(total).setScale(2, RoundingMode.HALF_UP);
    }

    private static double distance(WarehouseOptimizeModels.RouteNode left, WarehouseOptimizeModels.RouteNode right) {
        double dx = safeDouble(right == null ? null : right.getX()) - safeDouble(left == null ? null : left.getX());
        double dy = safeDouble(right == null ? null : right.getY()) - safeDouble(left == null ? null : left.getY());
        double levelPenalty = Math.abs((right == null ? 0 : safeLevel(right.getLevel())) - (left == null ? 0 : safeLevel(left.getLevel()))) * 0.35;
        return Math.sqrt((dx * dx) + (dy * dy)) + levelPenalty;
    }

    private static BigDecimal calculateImprovement(BigDecimal originalDistance, BigDecimal optimizedDistance) {
        if (originalDistance == null || optimizedDistance == null || BigDecimal.ZERO.compareTo(originalDistance) == 0) {
            return BigDecimal.ZERO;
        }
        BigDecimal saved = originalDistance.subtract(optimizedDistance);
        return saved.multiply(new BigDecimal("100"))
                .divide(originalDistance, 2, RoundingMode.HALF_UP);
    }

    private static int velocityRank(String velocityClass) {
        if ("A".equalsIgnoreCase(velocityClass)) return 0;
        if ("B".equalsIgnoreCase(velocityClass)) return 1;
        return 2;
    }

    private static String safeVelocity(String velocityClass) {
        return velocityClass == null || velocityClass.trim().isEmpty() ? "C" : velocityClass.trim().toUpperCase();
    }

    private static BigDecimal zeroIfNull(BigDecimal value) {
        return value == null ? BigDecimal.ZERO : value;
    }

    private static int compareNullable(BigDecimal left, BigDecimal right) {
        return zeroIfNull(left).compareTo(zeroIfNull(right));
    }

    private static int safeLevel(Integer level) {
        return level == null ? 1 : level;
    }

    private static double safeDouble(BigDecimal value) {
        return value == null ? 0d : value.doubleValue();
    }

    private static class RouteCandidate {
        private final String method;
        private final List<String> route;

        private RouteCandidate(String method, List<String> route) {
            this.method = method;
            this.route = route;
        }
    }
}
