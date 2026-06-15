/**
 * AisleDrawer - Interactive canvas-based warehouse aisle drawing tool
 * Handles mouse interactions, aisle creation, selection, zoom/pan, and rendering
 */

class AisleDrawer {
    constructor(canvas, warehouseWidth, warehouseHeight) {
        console.log('AisleDrawer constructor called with:', { canvas, warehouseWidth, warehouseHeight });

        if (!canvas) {
            console.error('Canvas element not provided to AisleDrawer');
            return;
        }

        this.canvas = canvas;
        this.ctx = canvas.getContext('2d');

        if (!this.ctx) {
            console.error('Could not get 2D context from canvas');
            return;
        }

        this.warehouseWidth = warehouseWidth;
        this.warehouseHeight = warehouseHeight;

        // Drawing state
        this._mode = 'draw'; // 'draw', 'select', 'delete', 'measure', 'move', 'boundary'
        this.isDrawing = false;
        this.startX = 0;
        this.startY = 0;
        this.currentX = 0;
        this.currentY = 0;

        // Aisles data
        this.aisles = [];
        this.selectedAisle = null;
        this.selectedAisles = []; // Multi-select support
        this.nextAisleId = 1;

        // Move mode state
        this.isMoving = false;
        this.moveStartX = 0;
        this.moveStartY = 0;
        this.movingAisle = null;
        this.movingAisles = []; // For moving multiple aisles
        this.moveOffsets = []; // Store initial offsets for each moving aisle

        // Configuration
        this.gridSize = 1; // 1 meter grid
        this.showGrid = true;
        this.snapToGrid = true;
        this.defaultType = 'HIGH_RACK';

        // Irregular warehouse boundary (polygon)
        this.boundaryPolygon = null;
        this.boundaryDraftPoints = [];
        this.boundaryHoverPoint = null;
        this.boundaryCloseDistance = 1.5;

        // Constraint model (v1)
        this.constraintTypes = new Set([
            'OBSTACLE',
            'PILLAR',
            'TUNNEL_ZONE',
            'WORKSTATION',
            'MHE_PARKING',
            'BATTERY_ROOM',
            'SAFETY_BUFFER',
            'OFFICE_AREA',
            'DOCK_DOOR'
        ]);
        this.storageTypes = new Set(['HIGH_RACK', 'SHELF', 'DRIVE_IN', 'FLOOR', 'STAGING']);

        // View settings
        this.showLocations = false;
        this.showLabels = false;
        this.showHeatmap = false;
        this.hoveredLocation = null;

        // Zoom and Pan
        this.zoom = 1.0;
        this.minZoom = 0.1;
        this.maxZoom = 5.0;
        this.panX = 0;
        this.panY = 0;
        this.isPanning = false;
        this.isSpacePressed = false;
        this.lastPanX = 0;
        this.lastPanY = 0;
        this.lastRotateAt = 0;

        // Measurement
        this.measureStart = null;
        this.measureEnd = null;
        this.isMeasuring = false;

        // History for undo/redo
        this.history = [];
        this.historyIndex = -1;
        this.maxHistorySize = 50;

        // Colors for different storage types
        this.colors = {
            'HIGH_RACK': '#3498db',
            'SHELF': '#2ecc71',
            'DRIVE_IN': '#9b59b6',
            'FLOOR': '#f39c12',
            'STAGING': '#1abc9c',
            'WORKING_STATUS': '#0ea5e9',
            'EMPTY_FLOOR': '#ecf0f1',
            'WALKWAY': '#3498db',
            'OBSTACLE': '#7f8c8d',
            'PILLAR': '#5d6d7e',
            'TUNNEL_ZONE': '#8e44ad',
            'WORKSTATION': '#16a085',
            'MHE_PARKING': '#d35400',
            'BATTERY_ROOM': '#c0392b',
            'SAFETY_BUFFER': '#f1c40f',
            'OFFICE_AREA': '#2c3e50',
            'DOCK_DOOR': '#5dade2',
            selected: '#e74c3c',
            grid: '#d5dbdb',
            gridMajor: '#99a3a4',
            boundary: '#2c3e50',
            preview: 'rgba(52, 152, 219, 0.3)',
            text: '#34495e',
            location: 'rgba(0, 0, 0, 0.8)',
            dimension: '#e67e22',
            measure: '#e74c3c',
            levelColors: [
                '#27ae60', // L1 - Green (ground)
                '#2ecc71', // L2
                '#f1c40f', // L3
                '#e67e22', // L4
                '#e74c3c', // L5
                '#9b59b6', // L6
                '#3498db', // L7
                '#1abc9c', // L8
                '#34495e', // L9
                '#95a5a6'  // L10
            ]
        };

        // Callbacks
        this.onAisleCreated = null;
        this.onAisleSelected = null;
        this.onMultipleAislesSelected = null; // Callback for multi-selection
        this.onAislesChanged = null;
        this.onZoomChanged = null;
        this.onCoordinatesChanged = null;

        this.setupCanvas();
        this.setupEventListeners();
        this.setupKeyboardShortcuts();
        this.saveState(); // Initial state
        this.applyConstraintFiltering();
        this.render();
    }

    set mode(value) {
        this.setMode(value);
    }

    get mode() {
        return this._mode;
    }

    setupCanvas() {
        // Set canvas size to fill container
        const container = this.canvas.parentElement;
        this.canvas.width = container.clientWidth;
        this.canvas.height = container.clientHeight - 50; // Leave room for hint

        // Calculate scale based on warehouse size
        this.baseScale = Math.min(
            this.canvas.width / this.warehouseWidth,
            this.canvas.height / this.warehouseHeight
        ) * 0.9;

        this.scale = this.baseScale * this.zoom;

        // Center the warehouse
        this.panX = (this.canvas.width - this.warehouseWidth * this.scale) / 2;
        this.panY = (this.canvas.height - this.warehouseHeight * this.scale) / 2;
    }

    setupEventListeners() {
        // Mouse events
        this.canvas.addEventListener('mousedown', (e) => this.handleMouseDown(e));
        this.canvas.addEventListener('mousemove', (e) => this.handleMouseMove(e));
        this.canvas.addEventListener('mouseup', (e) => this.handleMouseUp(e));
        this.canvas.addEventListener('mouseleave', (e) => this.handleMouseLeave(e));
        this.canvas.addEventListener('click', (e) => this.handleClick(e));

        // Zoom with mouse wheel
        this.canvas.addEventListener('wheel', (e) => this.handleWheel(e));

        // Prevent context menu
        this.canvas.addEventListener('contextmenu', (e) => e.preventDefault());

        // Window resize
        window.addEventListener('resize', () => {
            this.setupCanvas();
            this.render();
        });
    }

    getCanvasScreenPoint(e) {
        const rect = this.canvas.getBoundingClientRect();
        const scaleX = rect.width > 0 ? this.canvas.width / rect.width : 1;
        const scaleY = rect.height > 0 ? this.canvas.height / rect.height : 1;
        const cssX = e.clientX - rect.left;
        const cssY = e.clientY - rect.top;

        return {
            rect,
            cssX,
            cssY,
            screenX: cssX * scaleX,
            screenY: cssY * scaleY
        };
    }

    getPointerCoords(e) {
        const point = this.getCanvasScreenPoint(e);
        const coords = this.screenToWarehouse(point.screenX, point.screenY);
        const snappedX = this.snapToGrid ? Math.round(coords.x / this.gridSize) * this.gridSize : coords.x;
        const snappedY = this.snapToGrid ? Math.round(coords.y / this.gridSize) * this.gridSize : coords.y;

        return {
            ...point,
            coords,
            snappedX,
            snappedY
        };
    }

    /**
     * Handle click to copy location name
     */
    handleClick(e) {
        if (this._mode === 'boundary') return;

        // Only handle left click
        if (e.button !== 0) return;

        const point = this.getCanvasScreenPoint(e);
        const coords = this.screenToWarehouse(point.screenX, point.screenY);

        // Find location at click position
        const location = this.findLocationAt(coords.x, coords.y);
        if (location) {
            // Copy to clipboard
            navigator.clipboard.writeText(location.location).then(() => {
                // Show toast notification if callback exists
                if (this.onLocationCopied) {
                    this.onLocationCopied(location.location);
                }
            }).catch(err => {
                console.error('Failed to copy location:', err);
            });
        }
    }

    /**
     * Find a location at given warehouse coordinates
     */
    findLocationAt(x, y) {
        for (const aisle of this.aisles) {
            for (const loc of aisle.locations) {
                const bayWidth = aisle.bayWidth || 1.2;
                const bayDepth = aisle.bayDepth || 1.0;

                // Check if click is within location bounds
                if (x >= loc.x && x <= loc.x + bayDepth &&
                    y >= loc.y && y <= loc.y + bayWidth) {
                    return loc;
                }
            }
        }
        return null;
    }

    setupKeyboardShortcuts() {
        document.addEventListener('keydown', (e) => {
            // Don't trigger shortcuts when typing in inputs
            if (e.target.tagName === 'INPUT' || e.target.tagName === 'SELECT' || e.target.tagName === 'TEXTAREA') {
                return;
            }

            switch (e.key.toLowerCase()) {
                case 'd':
                    this.setMode('draw');
                    e.preventDefault();
                    break;
                case 's':
                    this.setMode('select');
                    e.preventDefault();
                    break;
                case 'm':
                    this.setMode('measure');
                    e.preventDefault();
                    break;
                case 'b':
                    this.setMode('boundary');
                    e.preventDefault();
                    break;
                case 'delete':
                case 'backspace':
                    if (this._mode === 'boundary' && this.clearBoundary()) {
                        e.preventDefault();
                        break;
                    }
                    if (this.selectedAisles.length > 0) {
                        this.deleteSelectedAisles();
                    } else if (this.selectedAisle) {
                        this.deleteAisle(this.selectedAisle);
                    }
                    e.preventDefault();
                    break;
                case 'z':
                    if (e.ctrlKey || e.metaKey) {
                        if (e.shiftKey) {
                            this.redo();
                        } else {
                            this.undo();
                        }
                        e.preventDefault();
                    }
                    break;
                case 'y':
                    if (e.ctrlKey || e.metaKey) {
                        this.redo();
                        e.preventDefault();
                    }
                    break;
                case 'g':
                    this.showGrid = !this.showGrid;
                    this.render();
                    e.preventDefault();
                    break;
                case 'l':
                    this.showLocations = !this.showLocations;
                    this.render();
                    e.preventDefault();
                    break;
                case 'escape':
                    this.cancelDrawing();
                    this.cancelBoundaryDraft();
                    this.selectedAisle = null;
                    this.selectedAisles = [];
                    this.measureStart = null;
                    this.measureEnd = null;
                    this.isMeasuring = false;
                    this.render();
                    break;
                case 'enter':
                    if (this._mode === 'boundary') {
                        this.finalizeBoundaryDraft();
                        e.preventDefault();
                    }
                    break;
                case 'a':
                    // Ctrl+A to select all aisles
                    if (e.ctrlKey || e.metaKey) {
                        this.selectAllAisles();
                        e.preventDefault();
                    }
                    break;
                case '+':
                case '=':
                    this.zoomIn();
                    e.preventDefault();
                    break;
                case '-':
                    this.zoomOut();
                    e.preventDefault();
                    break;
                case '0':
                    if (e.ctrlKey || e.metaKey) {
                        this.zoomToFit();
                        e.preventDefault();
                    }
                    break;
                case ' ':
                    // Space to pan
                    this.isSpacePressed = true;
                    this.canvas.classList.add('panning');
                    e.preventDefault();
                    break;
            }
        });

        document.addEventListener('keyup', (e) => {
            if (e.key === ' ') {
                this.isSpacePressed = false;
                this.canvas.classList.remove('panning');
            }
        });
    }

    setMode(mode) {
        this._mode = mode;
        this.cancelDrawing();
        this.measureStart = null;
        this.measureEnd = null;
        this.isMeasuring = false;
        this.isMoving = false;
        this.movingAisle = null;
        this.movingAisles = [];
        this.moveOffsets = [];
        if (mode !== 'boundary') {
            this.cancelBoundaryDraft();
        }

        // Update UI buttons
        document.querySelectorAll('.btn-mode').forEach(btn => {
            btn.classList.toggle('active', btn.dataset.mode === mode);
        });

        // Update cursor
        this.canvas.className = mode === 'select' ? 'select-mode' :
            mode === 'delete' ? 'delete-mode' :
                mode === 'measure' ? 'measure-mode' :
                    mode === 'move' ? 'move-mode' :
                        mode === 'boundary' ? 'draw-mode' : '';

        this.render();
    }

    // Zoom functions
    zoomIn() {
        this.setZoom(this.zoom * 1.2);
    }

    zoomOut() {
        this.setZoom(this.zoom / 1.2);
    }

    setZoom(newZoom, centerX = null, centerY = null) {
        const oldZoom = this.zoom;
        this.zoom = Math.max(this.minZoom, Math.min(this.maxZoom, newZoom));
        this.scale = this.baseScale * this.zoom;

        if (centerX === null || centerY === null) {
            centerX = this.canvas.width / 2;
            centerY = this.canvas.height / 2;
        }

        // Zoom towards center point
        const zoomRatio = this.zoom / oldZoom;
        this.panX = centerX - (centerX - this.panX) * zoomRatio;
        this.panY = centerY - (centerY - this.panY) * zoomRatio;

        if (this.onZoomChanged) {
            this.onZoomChanged(Math.round(this.zoom * 100));
        }

        this.render();
    }

    zoomToFit() {
        this.zoom = 1.0;
        this.scale = this.baseScale;
        this.panX = (this.canvas.width - this.warehouseWidth * this.scale) / 2;
        this.panY = (this.canvas.height - this.warehouseHeight * this.scale) / 2;

        if (this.onZoomChanged) {
            this.onZoomChanged(100);
        }

        this.render();
    }

    // Convert screen coordinates to warehouse coordinates
    // Y is flipped so Y=0 is at bottom-left (like a floor plan)
    // Render transform: translate(panX, panY) -> scale(s, s) -> translate(0, h) -> scale(1, -1)
    // So: screenX = panX + scale * warehouseX
    //     screenY = panY + scale * (warehouseHeight - warehouseY)
    // Inverting: warehouseX = (screenX - panX) / scale
    //            warehouseY = warehouseHeight - (screenY - panY) / scale
    screenToWarehouse(screenX, screenY) {
        const warehouseX = (screenX - this.panX) / this.scale;
        const warehouseY = this.warehouseHeight - (screenY - this.panY) / this.scale;
        return { x: warehouseX, y: warehouseY };
    }

    // Convert warehouse coordinates to screen coordinates
    // Y is flipped so Y=0 is at bottom-left (like a floor plan)
    warehouseToScreen(warehouseX, warehouseY) {
        const screenX = this.panX + this.scale * warehouseX;
        const screenY = this.panY + this.scale * (this.warehouseHeight - warehouseY);
        return { x: screenX, y: screenY };
    }

    // Helper to draw text right-side-up when Y-axis is flipped
    drawFlippedText(text, x, y, options = {}) {
        this.ctx.save();
        this.ctx.translate(x, y);
        this.ctx.scale(1, -1); // Flip back for text

        if (options.font) this.ctx.font = options.font;
        if (options.fillStyle) this.ctx.fillStyle = options.fillStyle;
        if (options.textAlign) this.ctx.textAlign = options.textAlign;
        if (options.textBaseline) this.ctx.textBaseline = options.textBaseline;

        this.ctx.fillText(text, 0, 0);
        this.ctx.restore();
    }

    handleWheel(e) {
        e.preventDefault();
        const point = this.getCanvasScreenPoint(e);

        if (
            this._mode === 'move' &&
            this.selectedAisles.length > 0 &&
            !e.ctrlKey &&
            !e.metaKey &&
            !e.altKey
        ) {
            const now = Date.now();
            if (now - this.lastRotateAt < 140) {
                return;
            }
            this.lastRotateAt = now;
            this.rotateSelectedAisles();
            return;
        }

        const zoomFactor = e.deltaY > 0 ? 0.9 : 1.1;
        this.setZoom(this.zoom * zoomFactor, point.screenX, point.screenY);
    }

    rotateSelectedAisles() {
        if (!this.selectedAisles.length) {
            return;
        }

        this.selectedAisles.forEach(aisle => aisle.rotate(this.warehouseWidth, this.warehouseHeight));
        this.applyConstraintFiltering();
        this.saveState();

        if (this.onAisleSelected && this.selectedAisle) {
            this.onAisleSelected(this.selectedAisle);
        }
        if (this.onMultipleAislesSelected && this.selectedAisles.length > 1) {
            this.onMultipleAislesSelected(this.selectedAisles);
        }
        if (this.onAislesChanged) {
            this.onAislesChanged();
        }

        this.render();
    }

    handleMouseDown(e) {
        // Disable right-click actions from affecting design tools
        if (e.button === 2) {
            e.preventDefault();
            return;
        }

        const pointer = this.getPointerCoords(e);
        const coords = pointer.coords;

        // Middle mouse button or Space+Click for panning
        if (e.button === 1 || (e.button === 0 && (this.isSpacePressed || e.getModifierState?.('Space')))) {
            this.isPanning = true;
            this.lastPanX = e.clientX;
            this.lastPanY = e.clientY;
            this.canvas.classList.add('panning');
            return;
        }

        // Snap to grid
        if (this._mode === 'draw') {
            this.startDrawing(coords.x, coords.y);
        } else if (this._mode === 'boundary') {
            this.addBoundaryPoint(coords.x, coords.y);
        } else if (this._mode === 'select') {
            this.selectAisleAt(coords.x, coords.y, e.ctrlKey || e.metaKey || e.shiftKey);
        } else if (this._mode === 'delete') {
            this.deleteAisleAt(coords.x, coords.y);
        } else if (this._mode === 'measure') {
            if (!this.isMeasuring) {
                this.measureStart = { x: coords.x, y: coords.y };
                this.measureEnd = { x: coords.x, y: coords.y };
                this.isMeasuring = true;
            }
        } else if (this._mode === 'move') {
            // Find aisle under cursor
            const aisle = this.findAisleAt(coords.x, coords.y);
            if (aisle) {
                // Check if clicking on an already selected aisle (for multi-move)
                const isAlreadySelected = this.selectedAisles.includes(aisle);

                if (isAlreadySelected && this.selectedAisles.length > 1) {
                    // Move all selected aisles together
                    this.movingAisles = [...this.selectedAisles];
                    this.moveOffsets = this.movingAisles.map(a => ({
                        aisle: a,
                        offsetX: pointer.snappedX - a.x,
                        offsetY: pointer.snappedY - a.y
                    }));
                } else {
                    // Single aisle move - add to selection if using modifier keys
                    if (e.ctrlKey || e.metaKey || e.shiftKey) {
                        if (!isAlreadySelected) {
                            this.selectedAisles.push(aisle);
                        }
                    } else {
                        // Replace selection with just this aisle
                        this.selectedAisles = [aisle];
                    }
                    this.movingAisles = [aisle];
                    this.moveOffsets = [{ aisle, offsetX: pointer.snappedX - aisle.x, offsetY: pointer.snappedY - aisle.y }];
                }

                this.movingAisle = aisle;
                this.selectedAisle = aisle;
                this.isMoving = true;
                this.moveStartX = pointer.snappedX - aisle.x;
                this.moveStartY = pointer.snappedY - aisle.y;
                this.canvas.classList.add('moving');
                this.render();
            }
        }
    }

    handleMouseMove(e) {
        const pointer = this.getPointerCoords(e);
        const coords = pointer.coords;

        // Update coordinates display
        if (this.onCoordinatesChanged) {
            this.onCoordinatesChanged(coords.x.toFixed(1), coords.y.toFixed(1));
        }

        // Panning
        if (this.isPanning) {
            this.panX += e.clientX - this.lastPanX;
            this.panY += e.clientY - this.lastPanY;
            this.lastPanX = e.clientX;
            this.lastPanY = e.clientY;
            this.render();
            if (this.onPanChanged) {
                this.onPanChanged();
            }
            return;
        }

        // Moving aisle(s)
        if (this.isMoving && this.movingAisles.length > 0) {
            const snappedX = this.snapToGrid ? Math.round(coords.x / this.gridSize) * this.gridSize : coords.x;
            const snappedY = this.snapToGrid ? Math.round(coords.y / this.gridSize) * this.gridSize : coords.y;

            // Move all selected aisles together
            for (const moveData of this.moveOffsets) {
                const aisle = moveData.aisle;
                const newX = snappedX - moveData.offsetX;
                const newY = snappedY - moveData.offsetY;

                // Clamp to warehouse bounds
                aisle.x = Math.max(0, Math.min(this.warehouseWidth - aisle.width, newX));
                aisle.y = Math.max(0, Math.min(this.warehouseHeight - aisle.height, newY));

                // Regenerate locations with new position
                aisle.generateLocations();
            }

            this.applyConstraintFiltering();

            this.render();
            return;
        }

        // Check for hovered location
        if (this.showLocations) {
            this.updateHoveredLocation(coords.x, coords.y, pointer.cssX, pointer.cssY);
        }

        if (this._mode === 'boundary' && this.boundaryDraftPoints.length > 0) {
            this.boundaryHoverPoint = { x: coords.x, y: coords.y };
            this.render();
            return;
        }

        this.currentX = coords.x;
        this.currentY = coords.y;

        if (this.isDrawing || this.isMeasuring) {
            if (this.isMeasuring) {
                this.measureEnd = { x: coords.x, y: coords.y };
            }
            this.render();
        }
    }

    handleMouseUp(e) {
        // Ignore right button releases (right-click is disabled for design actions)
        if (e.button === 2) {
            e.preventDefault();
            return;
        }

        if (this.isPanning) {
            this.isPanning = false;
            this.canvas.classList.remove('panning');
            return;
        }

        // Finish moving
        if (this.isMoving && this.movingAisles.length > 0) {
            this.isMoving = false;
            this.canvas.classList.remove('moving');
            this.saveState();
            if (this.onAisleSelected) {
                this.onAisleSelected(this.movingAisle);
            }
            if (this.onAislesChanged) {
                this.onAislesChanged();
            }
            this.movingAisle = null;
            this.movingAisles = [];
            this.moveOffsets = [];
            this.render();
            return;
        }

        if (this.isDrawing) {
            this.finishDrawing();
        }
    }

    handleMouseLeave(e) {
        if (this.isPanning) {
            this.isPanning = false;
            this.canvas.classList.remove('panning');
        }
        if (this.isMoving) {
            this.isMoving = false;
            this.canvas.classList.remove('moving');
            this.movingAisle = null;
            this.movingAisles = [];
            this.moveOffsets = [];
        }
        if (this.isDrawing) {
            this.cancelDrawing();
        }
        this.hoveredLocation = null;
        this.boundaryHoverPoint = null;
        this.hideLocationTooltip();
    }

    updateHoveredLocation(x, y, screenX, screenY) {
        let found = null;

        for (const aisle of this.aisles) {
            for (const loc of aisle.locations) {
                const dims = this.getLocationDimensions(aisle.type);
                if (x >= loc.x && x <= loc.x + dims.width &&
                    y >= loc.y && y <= loc.y + dims.depth) {
                    found = loc;
                    break;
                }
            }
            if (found) break;
        }

        if (found !== this.hoveredLocation) {
            this.hoveredLocation = found;
            if (found) {
                this.showLocationTooltip(found, screenX, screenY);
            } else {
                this.hideLocationTooltip();
            }
        }
    }

    showLocationTooltip(location, x, y) {
        const tooltip = document.getElementById('locationTooltip');
        if (!tooltip) return;

        let slottingInfo = '';
        if (location.slottedSku) {
            const classColors = { A: '#e74c3c', B: '#f39c12', C: '#3498db' };
            slottingInfo = `
                <div class="tooltip-row" style="margin-top: 5px; padding-top: 5px; border-top: 1px dashed #ddd;">
                    <span class="tooltip-label">Product:</span> <strong>${location.slottedSku}</strong>
                </div>
                <div class="tooltip-row">
                    <span class="tooltip-label">Velocity:</span> 
                    <span style="background: ${classColors[location.slottedClass] || '#999'}; color: white; padding: 1px 6px; border-radius: 3px; font-size: 10px;">${location.slottedClass}-Class</span>
                </div>
            `;
        }

        tooltip.innerHTML = `
            <div class="tooltip-header">${location.location}</div>
            <div class="tooltip-row"><span class="tooltip-label">Zone:</span> ${location.zone}</div>
            <div class="tooltip-row"><span class="tooltip-label">Type:</span> ${location.type}</div>
            <div class="tooltip-row"><span class="tooltip-label">Level:</span> ${location.level}</div>
            <div class="tooltip-row"><span class="tooltip-label">Position:</span> (${location.x.toFixed(1)}, ${location.y.toFixed(1)})</div>
            ${slottingInfo}
        `;
        tooltip.style.display = 'block';
        tooltip.style.left = (x + 15) + 'px';
        tooltip.style.top = (y + 15) + 'px';
    }

    hideLocationTooltip() {
        const tooltip = document.getElementById('locationTooltip');
        if (tooltip) {
            tooltip.style.display = 'none';
        }
    }

    getLocationDimensions(type) {
        const dims = {
            'HIGH_RACK': { width: 1.2, depth: 1.0, aisleWidth: 2.5 },
            'SHELF': { width: 1.0, depth: 0.6, aisleWidth: 1.8 },
            'DRIVE_IN': { width: 1.2, depth: 1.2, aisleWidth: 0 },
            'FLOOR': { width: 1.5, depth: 1.5, aisleWidth: 3.0 },
            'STAGING': { width: 2.0, depth: 2.0, aisleWidth: 3.0 },
            'DOCK_DOOR': { width: 3.2, depth: 1.4, aisleWidth: 0 }
        };
        return dims[type] || dims['HIGH_RACK'];
    }

    getNearestPerimeterEdge(rect, boundsWidth = this.warehouseWidth, boundsHeight = this.warehouseHeight) {
        const distances = [
            { edge: 'left', distance: Math.abs(rect.x) },
            { edge: 'right', distance: Math.abs(boundsWidth - (rect.x + rect.width)) },
            { edge: 'bottom', distance: Math.abs(rect.y) },
            { edge: 'top', distance: Math.abs(boundsHeight - (rect.y + rect.height)) }
        ];

        distances.sort((a, b) => a.distance - b.distance);
        return distances[0].edge;
    }

    saveState() {
        const state = {
            aisles: this.aisles.map(aisle => ({
                ...aisle,
                locations: [...aisle.locations],
                baseLocations: [...(aisle.baseLocations || [])],
                tunnelRules: []
            })),
            boundaryPolygon: this.boundaryPolygon ? this.boundaryPolygon.map(p => ({ ...p })) : null,
            selectedAisle: this.selectedAisle ? this.selectedAisle.id : null,
            selectedAisles: this.selectedAisles.map(a => a.id)
        };

        this.history = this.history.slice(0, this.historyIndex + 1);
        this.history.push(state);
        this.historyIndex++;

        if (this.history.length > this.maxHistorySize) {
            this.history.shift();
            this.historyIndex--;
        }
    }

    undo() {
        if (this.historyIndex > 0) {
            this.historyIndex--;
            this.restoreState(this.history[this.historyIndex]);
        }
    }

    redo() {
        if (this.historyIndex < this.history.length - 1) {
            this.historyIndex++;
            this.restoreState(this.history[this.historyIndex]);
        }
    }

    restoreState(state) {
        this.aisles = state.aisles.map(aisleData => {
            const aisle = new Aisle(
                aisleData.id,
                aisleData.x,
                aisleData.y,
                aisleData.width,
                aisleData.height,
                aisleData.type,
                aisleData.levels,
                aisleData.zone
            );
            aisle.locations = aisleData.locations;
            aisle.baseLocations = aisleData.baseLocations || [...aisle.locations];
            aisle.bayWidth = aisleData.bayWidth;
            aisle.bayDepth = aisleData.bayDepth;
            aisle.aisleWidth = aisleData.aisleWidth;
            aisle.direction = aisleData.direction || 'AUTO';
            aisle.tunnelLevelFrom = aisleData.tunnelLevelFrom || 1;
            aisle.tunnelLevelTo = aisleData.tunnelLevelTo || aisleData.levels || 1;
            aisle.tunnelRules = [];
            return aisle;
        });

        this.selectedAisle = state.selectedAisle ?
            this.aisles.find(a => a.id === state.selectedAisle) : null;

        this.boundaryPolygon = Array.isArray(state.boundaryPolygon)
            ? state.boundaryPolygon.map(p => ({ ...p }))
            : null;

        // Restore multi-selection
        this.selectedAisles = (state.selectedAisles || [])
            .map(id => this.aisles.find(a => a.id === id))
            .filter(a => a); // Filter out any not found

        if (this.onAislesChanged) {
            this.onAislesChanged();
        }

        this.applyConstraintFiltering();

        this.render();
    }

    startDrawing(x, y) {
        this.isDrawing = true;
        this.startX = x;
        this.startY = y;
        this.currentX = x;
        this.currentY = y;
    }

    finishDrawing() {
        if (!this.isDrawing) return;

        const width = Math.abs(this.currentX - this.startX);
        const height = Math.abs(this.currentY - this.startY);

        if (width < 3 || height < 3) {
            this.cancelDrawing();
            return;
        }

        const aisle = new Aisle(
            this.nextAisleId++,
            Math.min(this.startX, this.currentX),
            Math.min(this.startY, this.currentY),
            width,
            height,
            this.defaultType,
            4, // default levels
            `Zone-${this.nextAisleId - 1}`
        );

        this.aisles.push(aisle);
        this.selectedAisle = aisle;
        this.selectedAisles = [aisle]; // Reset to just the new aisle
        this.isDrawing = false;

        aisle.generateLocations();
        this.applyConstraintFiltering();
        this.saveState();

        if (this.onAisleCreated) {
            this.onAisleCreated(aisle);
        }
        if (this.onAislesChanged) {
            this.onAislesChanged();
        }

        this.render();
    }

    cancelDrawing() {
        this.isDrawing = false;
        this.render();
    }

    findAisleAt(x, y) {
        return this.aisles.find(a => a.containsPoint(x, y)) || null;
    }

    selectAisleAt(x, y, addToSelection = false) {
        const aisle = this.findAisleAt(x, y);

        if (addToSelection) {
            // Multi-select mode (Ctrl/Shift held)
            if (aisle) {
                const index = this.selectedAisles.indexOf(aisle);
                if (index === -1) {
                    // Add to selection
                    this.selectedAisles.push(aisle);
                } else {
                    // Remove from selection if already selected
                    this.selectedAisles.splice(index, 1);
                }
                this.selectedAisle = this.selectedAisles.length > 0 ? this.selectedAisles[this.selectedAisles.length - 1] : null;
            }
        } else {
            // Single select mode
            if (aisle !== this.selectedAisle || this.selectedAisles.length > 1) {
                this.selectedAisle = aisle;
                this.selectedAisles = aisle ? [aisle] : [];
            }
        }

        // Notify about selection
        if (this.selectedAisles.length > 1 && this.onMultipleAislesSelected) {
            this.onMultipleAislesSelected(this.selectedAisles);
        } else if (this.onAisleSelected && this.selectedAisle) {
            this.onAisleSelected(this.selectedAisle);
        }

        this.render();
    }

    /**
     * Select all aisles (Ctrl+A)
     */
    selectAllAisles() {
        this.selectedAisles = [...this.aisles];
        this.selectedAisle = this.selectedAisles.length > 0 ? this.selectedAisles[0] : null;

        if (this.selectedAisles.length > 1 && this.onMultipleAislesSelected) {
            this.onMultipleAislesSelected(this.selectedAisles);
        } else if (this.onAisleSelected && this.selectedAisle) {
            this.onAisleSelected(this.selectedAisle);
        }

        this.render();
    }

    /**
     * Check if an aisle is in the current selection
     */
    isAisleSelected(aisle) {
        return this.selectedAisles.includes(aisle) || aisle === this.selectedAisle;
    }

    hasBoundary() {
        return Array.isArray(this.boundaryPolygon) && this.boundaryPolygon.length >= 3;
    }

    hasBoundaryDraft() {
        return this.boundaryDraftPoints.length > 0 || !!this.boundaryHoverPoint;
    }

    hasDesignContent() {
        return this.aisles.length > 0 || this.hasBoundary() || this.hasBoundaryDraft();
    }

    clearBoundary() {
        if (!this.hasBoundary() && !this.hasBoundaryDraft()) {
            return false;
        }

        this.boundaryPolygon = null;
        this.cancelBoundaryDraft();
        this.applyConstraintFiltering();
        this.saveState();

        if (this.onAislesChanged) {
            this.onAislesChanged();
        }

        this.render();
        return true;
    }

    clearAll() {
        if (!this.hasDesignContent()) {
            return false;
        }

        this.aisles = [];
        this.selectedAisle = null;
        this.selectedAisles = [];
        this.boundaryPolygon = null;
        this.cancelBoundaryDraft();
        this.hoveredLocation = null;
        this.applyConstraintFiltering();
        this.saveState();

        if (this.onAislesChanged) {
            this.onAislesChanged();
        }

        this.render();
        return true;
    }

    resizeWarehouseBounds(nextWidth, nextHeight) {
        const width = Math.max(10, Number(nextWidth) || this.warehouseWidth);
        const height = Math.max(10, Number(nextHeight) || this.warehouseHeight);

        this.warehouseWidth = width;
        this.warehouseHeight = height;

        let movedAisles = 0;
        let resizedAisles = 0;
        let adjustedBoundaryPoints = 0;

        for (const aisle of this.aisles) {
            let changed = false;
            let resized = false;

            if (aisle.width > width) {
                aisle.width = width;
                resized = true;
                changed = true;
            }

            if (aisle.height > height) {
                aisle.height = height;
                resized = true;
                changed = true;
            }

            const maxX = Math.max(0, width - aisle.width);
            const maxY = Math.max(0, height - aisle.height);
            const clampedX = Math.max(0, Math.min(maxX, aisle.x));
            const clampedY = Math.max(0, Math.min(maxY, aisle.y));

            if (clampedX !== aisle.x || clampedY !== aisle.y) {
                aisle.x = clampedX;
                aisle.y = clampedY;
                movedAisles += 1;
                changed = true;
            }

            if (changed) {
                if (resized) {
                    resizedAisles += 1;
                }
                aisle.generateLocations();
            }
        }

        if (this.hasBoundary()) {
            this.boundaryPolygon = this.boundaryPolygon.map(point => {
                const clampedPoint = {
                    x: Math.max(0, Math.min(width, point.x)),
                    y: Math.max(0, Math.min(height, point.y))
                };
                if (clampedPoint.x !== point.x || clampedPoint.y !== point.y) {
                    adjustedBoundaryPoints += 1;
                }
                return clampedPoint;
            });
        }

        this.applyConstraintFiltering();
        this.setupCanvas();
        this.saveState();

        if (this.onAislesChanged) {
            this.onAislesChanged();
        }

        this.render();
        return {
            movedAisles,
            resizedAisles,
            adjustedBoundaryPoints
        };
    }

    distancePointToSegment(point, start, end) {
        const dx = end.x - start.x;
        const dy = end.y - start.y;
        if (dx === 0 && dy === 0) {
            return Math.hypot(point.x - start.x, point.y - start.y);
        }

        const projection = ((point.x - start.x) * dx + (point.y - start.y) * dy) / ((dx * dx) + (dy * dy));
        const t = Math.max(0, Math.min(1, projection));
        const nearestX = start.x + (t * dx);
        const nearestY = start.y + (t * dy);

        return Math.hypot(point.x - nearestX, point.y - nearestY);
    }

    isPointOnBoundary(point, tolerance = Math.max(this.boundaryCloseDistance, 12 / this.scale)) {
        if (!this.hasBoundary()) {
            return false;
        }

        for (let i = 0; i < this.boundaryPolygon.length; i++) {
            const start = this.boundaryPolygon[i];
            const end = this.boundaryPolygon[(i + 1) % this.boundaryPolygon.length];
            if (this.distancePointToSegment(point, start, end) <= tolerance) {
                return true;
            }
        }

        return false;
    }

    deleteAisleAt(x, y) {
        const index = this.aisles.findIndex(a => a.containsPoint(x, y));

        if (index !== -1) {
            const deleted = this.aisles.splice(index, 1)[0];

            if (this.selectedAisle && this.selectedAisle.id === deleted.id) {
                this.selectedAisle = null;
            }

            // Remove from multi-selection as well
            const selIndex = this.selectedAisles.indexOf(deleted);
            if (selIndex !== -1) {
                this.selectedAisles.splice(selIndex, 1);
            }

            this.applyConstraintFiltering();
            this.saveState();

            if (this.onAislesChanged) {
                this.onAislesChanged();
            }

            this.render();
            return true;
        }

        if (this.isPointOnBoundary({ x, y })) {
            return this.clearBoundary();
        }

        return false;
    }

    deleteAisle(aisle) {
        const index = this.aisles.indexOf(aisle);
        if (index !== -1) {
            this.aisles.splice(index, 1);
            if (this.selectedAisle === aisle) {
                this.selectedAisle = null;
            }

            // Remove from multi-selection as well
            const selIndex = this.selectedAisles.indexOf(aisle);
            if (selIndex !== -1) {
                this.selectedAisles.splice(selIndex, 1);
            }

            this.applyConstraintFiltering();
            this.saveState();

            if (this.onAislesChanged) {
                this.onAislesChanged();
            }

            this.render();
        }
    }

    /**
     * Delete all selected aisles
     */
    deleteSelectedAisles() {
        if (this.selectedAisles.length === 0) return;

        // Remove all selected aisles
        for (const aisle of this.selectedAisles) {
            const index = this.aisles.indexOf(aisle);
            if (index !== -1) {
                this.aisles.splice(index, 1);
            }
        }

        this.selectedAisle = null;
        this.selectedAisles = [];

        this.applyConstraintFiltering();
        this.saveState();

        if (this.onAislesChanged) {
            this.onAislesChanged();
        }

        this.render();
    }

    duplicateAisle(aisle) {
        const newAisle = new Aisle(
            this.nextAisleId++,
            aisle.x + 5,
            aisle.y + 5,
            aisle.width,
            aisle.height,
            aisle.type,
            aisle.levels,
            aisle.zone + '-copy'
        );
        newAisle.bayWidth = aisle.bayWidth;
        newAisle.bayDepth = aisle.bayDepth;
        newAisle.aisleWidth = aisle.aisleWidth;
        newAisle.direction = aisle.direction || 'AUTO';
        newAisle.pickFaceLevels = Array.isArray(aisle.pickFaceLevels) ? [...aisle.pickFaceLevels] : undefined;
        newAisle.startPointX = aisle.startPointX;
        newAisle.startPointY = aisle.startPointY;
        newAisle.tunnelLevelFrom = aisle.tunnelLevelFrom || 1;
        newAisle.tunnelLevelTo = aisle.tunnelLevelTo || aisle.levels || 1;
        newAisle.workingStatusFlow = aisle.workingStatusFlow || null;
        newAisle.workingStatusCode = aisle.workingStatusCode || null;
        newAisle.workingStatusLabel = aisle.workingStatusLabel || null;
        newAisle.workingStatusColor = aisle.workingStatusColor || null;
        newAisle.tunnelRules = [];
        newAisle.generateLocations();

        this.aisles.push(newAisle);
        this.selectedAisle = newAisle;
        this.selectedAisles = [newAisle];
        this.applyConstraintFiltering();
        this.saveState();

        if (this.onAisleCreated) {
            this.onAisleCreated(newAisle);
        }
        if (this.onAislesChanged) {
            this.onAislesChanged();
        }

        this.render();
        return newAisle;
    }

    getAllLocations() {
        let allLocations = [];
        this.aisles.forEach(aisle => {
            allLocations = allLocations.concat(aisle.locations);
        });
        return allLocations;
    }

    // Rendering
    render() {
        this.ctx.save();

        // Clear canvas
        this.ctx.fillStyle = '#fafafa';
        this.ctx.fillRect(0, 0, this.canvas.width, this.canvas.height);

        // Apply pan and zoom, with Y-axis flipped (Y=0 at bottom)
        this.ctx.translate(this.panX, this.panY);
        this.ctx.scale(this.scale, this.scale);

        // Flip Y-axis: translate to bottom and scale Y by -1
        this.ctx.translate(0, this.warehouseHeight);
        this.ctx.scale(1, -1);

        // Draw warehouse boundary background FIRST
        this.drawBoundaryBackground();

        // Draw grid if enabled (on top of background)
        if (this.showGrid) {
            this.drawGrid();
        }

        // Draw warehouse boundary lines
        this.drawBoundaryLines();

        // Draw existing aisles
        this.aisles.forEach(aisle => {
            this.drawAisle(aisle);
        });

        // Draw location boxes if enabled
        if (this.showLocations) {
            this.aisles.forEach(aisle => {
                this.drawLocationBoxes(aisle);
            });
        }

        if (this._mode === 'boundary' || this.boundaryDraftPoints.length > 0) {
            this.drawBoundaryDraft();
        }

        // Draw current drawing preview
        if (this.isDrawing) {
            this.drawDrawingPreview();
        }

        // Draw measurement
        if (this.measureStart && this.measureEnd) {
            this.drawMeasurement();
        }

        this.ctx.restore();

        // Draw UI elements (not affected by zoom)
        this.drawInstructions();
    }

    drawGrid() {
        // Minor grid
        this.ctx.strokeStyle = this.colors.grid;
        this.ctx.lineWidth = 0.5 / this.scale;

        for (let x = 0; x <= this.warehouseWidth; x += this.gridSize) {
            this.ctx.beginPath();
            this.ctx.moveTo(x, 0);
            this.ctx.lineTo(x, this.warehouseHeight);
            this.ctx.stroke();
        }

        for (let y = 0; y <= this.warehouseHeight; y += this.gridSize) {
            this.ctx.beginPath();
            this.ctx.moveTo(0, y);
            this.ctx.lineTo(this.warehouseWidth, y);
            this.ctx.stroke();
        }

        // Major grid (every 10m)
        this.ctx.strokeStyle = this.colors.gridMajor;
        this.ctx.lineWidth = 1 / this.scale;

        for (let x = 0; x <= this.warehouseWidth; x += 10) {
            this.ctx.beginPath();
            this.ctx.moveTo(x, 0);
            this.ctx.lineTo(x, this.warehouseHeight);
            this.ctx.stroke();
        }

        for (let y = 0; y <= this.warehouseHeight; y += 10) {
            this.ctx.beginPath();
            this.ctx.moveTo(0, y);
            this.ctx.lineTo(this.warehouseWidth, y);
            this.ctx.stroke();
        }
    }

    drawBoundaryBackground() {
        // Keep the floor appearance stable; boundary is represented by lines/objects, not fill color.
        this.ctx.fillStyle = 'white';
        this.ctx.fillRect(0, 0, this.warehouseWidth, this.warehouseHeight);
    }

    drawBoundaryLines() {
        // Always show full rectangular frame
        this.ctx.strokeStyle = '#b0b8bf';
        this.ctx.lineWidth = 1 / this.scale;
        this.ctx.setLineDash([4 / this.scale, 4 / this.scale]);
        this.ctx.strokeRect(0, 0, this.warehouseWidth, this.warehouseHeight);
        this.ctx.setLineDash([]);

        // Draw active boundary (polygon or rectangular default)
        this.ctx.strokeStyle = this.colors.boundary;
        this.ctx.lineWidth = 2 / this.scale;
        if (Array.isArray(this.boundaryPolygon) && this.boundaryPolygon.length >= 3) {
            this.ctx.beginPath();
            this.ctx.moveTo(this.boundaryPolygon[0].x, this.boundaryPolygon[0].y);
            for (let i = 1; i < this.boundaryPolygon.length; i++) {
                this.ctx.lineTo(this.boundaryPolygon[i].x, this.boundaryPolygon[i].y);
            }
            this.ctx.closePath();
            this.ctx.stroke();
        } else {
            this.ctx.strokeRect(0, 0, this.warehouseWidth, this.warehouseHeight);
        }

        // Draw dimension labels (need to flip for text)
        const fontSize = 12 / this.scale;

        // Width label at top (which is now visually at bottom due to flip)
        this.drawFlippedText(
            `${this.warehouseWidth}m`,
            this.warehouseWidth / 2,
            this.warehouseHeight + 15 / this.scale,
            { font: `${fontSize}px Arial`, fillStyle: this.colors.text, textAlign: 'center', textBaseline: 'middle' }
        );

        // Height label on left side
        this.ctx.save();
        this.ctx.translate(-15 / this.scale, this.warehouseHeight / 2);
        this.ctx.scale(1, -1);
        this.ctx.rotate(Math.PI / 2);
        this.ctx.font = `${fontSize}px Arial`;
        this.ctx.fillStyle = this.colors.text;
        this.ctx.textAlign = 'center';
        this.ctx.fillText(`${this.warehouseHeight}m`, 0, 0);
        this.ctx.restore();

        // Draw origin indicator (0,0) at bottom-left
        this.drawFlippedText(
            '(0,0)',
            -10 / this.scale,
            -10 / this.scale,
            { font: `${10 / this.scale}px Arial`, fillStyle: '#e74c3c', textAlign: 'right', textBaseline: 'top' }
        );
    }

    isConstraintType(type) {
        return this.constraintTypes.has(type);
    }

    isStorageType(type) {
        return this.storageTypes.has(type);
    }

    rectsIntersect(a, b) {
        return !(
            a.x + a.width <= b.x ||
            b.x + b.width <= a.x ||
            a.y + a.height <= b.y ||
            b.y + b.height <= a.y
        );
    }

    getLocationRect(location, aisle) {
        const dims = this.getLocationDimensions(aisle.type);
        let width = dims.width;
        let depth = dims.depth;

        if (aisle.type === 'FLOOR' || aisle.type === 'STAGING') {
            width = aisle.bayDepth || dims.depth;
            depth = aisle.bayWidth || dims.width;
        }

        return {
            x: location.x,
            y: location.y,
            width: Math.max(0.05, width * 0.9),
            height: Math.max(0.05, depth * 0.9)
        };
    }

    getConstraintAisles() {
        return this.aisles.filter(a => this.isConstraintType(a.type) && a.type !== 'TUNNEL_ZONE');
    }

    pointInPolygon(point, polygon) {
        if (!Array.isArray(polygon) || polygon.length < 3) return true;

        let inside = false;
        for (let i = 0, j = polygon.length - 1; i < polygon.length; j = i++) {
            const xi = polygon[i].x;
            const yi = polygon[i].y;
            const xj = polygon[j].x;
            const yj = polygon[j].y;

            const intersects = ((yi > point.y) !== (yj > point.y)) &&
                (point.x < ((xj - xi) * (point.y - yi)) / ((yj - yi) || 1e-9) + xi);
            if (intersects) inside = !inside;
        }
        return inside;
    }

    isPointInsideBoundary(point) {
        if (!Array.isArray(this.boundaryPolygon) || this.boundaryPolygon.length < 3) {
            return point.x >= 0 && point.x <= this.warehouseWidth && point.y >= 0 && point.y <= this.warehouseHeight;
        }
        return this.pointInPolygon(point, this.boundaryPolygon);
    }

    isRectInsideBoundary(rect) {
        const corners = [
            { x: rect.x, y: rect.y },
            { x: rect.x + rect.width, y: rect.y },
            { x: rect.x, y: rect.y + rect.height },
            { x: rect.x + rect.width, y: rect.y + rect.height }
        ];
        return corners.every(c => this.isPointInsideBoundary(c));
    }

    applyConstraintFiltering() {
        const constraints = this.getConstraintAisles();
        const tunnelZones = this.aisles.filter(a => a.type === 'TUNNEL_ZONE');

        this.aisles.forEach(aisle => {
            if (!this.isStorageType(aisle.type)) return;

            if (!Array.isArray(aisle.baseLocations) || aisle.baseLocations.length === 0) {
                aisle.baseLocations = aisle.locations.map(loc => ({ ...loc }));
            }

            aisle.locations = aisle.baseLocations.filter(loc => {
                const locRect = this.getLocationRect(loc, aisle);
                if (!this.isRectInsideBoundary(locRect)) {
                    return false;
                }
                if (constraints.length > 0 && constraints.some(constraint => this.rectsIntersect(locRect, constraint))) {
                    return false;
                }
                if (tunnelZones.length === 0) {
                    return true;
                }

                const locLevel = Number(loc.level || 1);
                return !tunnelZones.some(tunnelZone => {
                    const tunnelLevelFrom = Math.max(1, Number(tunnelZone.tunnelLevelFrom || 1));
                    const tunnelLevelTo = Math.max(tunnelLevelFrom, Number(tunnelZone.tunnelLevelTo || tunnelZone.levels || 1));
                    return locLevel >= tunnelLevelFrom && locLevel <= tunnelLevelTo && this.rectsIntersect(locRect, tunnelZone);
                });
            }).map(loc => ({ ...loc }));
        });
    }

    getConstraintImpactSummary() {
        let baseCount = 0;
        let activeCount = 0;

        this.aisles.forEach(aisle => {
            if (!this.isStorageType(aisle.type)) return;
            baseCount += (aisle.baseLocations || aisle.locations || []).length;
            activeCount += (aisle.locations || []).length;
        });

        return {
            baseCount,
            activeCount,
            excludedCount: Math.max(0, baseCount - activeCount)
        };
    }

    drawAisle(aisle) {
        const isSelected = this.isAisleSelected(aisle);

        if (aisle.type === 'DOCK_DOOR') {
            this.drawDockDoorArea(aisle, isSelected);
            if (isSelected) {
                this.drawDimensions(aisle.x, aisle.y, aisle.width, aisle.height);
            }
            return;
        }

        if (this.isConstraintType(aisle.type)) {
            this.drawConstraintArea(aisle, isSelected);
            if (isSelected) {
                this.drawDimensions(aisle.x, aisle.y, aisle.width, aisle.height);
            }
            return;
        }

        if (aisle.type === 'WALKWAY') {
            this.ctx.fillStyle = '#3498db';
            this.ctx.globalAlpha = isSelected ? 0.8 : 0.6;
            this.ctx.fillRect(aisle.x, aisle.y, aisle.width, aisle.height);

            this.ctx.strokeStyle = '#f1c40f';
            this.ctx.lineWidth = 3 / this.scale;
            this.ctx.setLineDash([10 / this.scale, 5 / this.scale]);
            this.ctx.globalAlpha = 1;

            this.ctx.beginPath();
            if (aisle.width > aisle.height) {
                const centerY = aisle.y + aisle.height / 2;
                this.ctx.moveTo(aisle.x, centerY);
                this.ctx.lineTo(aisle.x + aisle.width, centerY);
            } else {
                const centerX = aisle.x + aisle.width / 2;
                this.ctx.moveTo(centerX, aisle.y);
                this.ctx.lineTo(centerX, aisle.y + aisle.height);
            }
            this.ctx.stroke();
            this.ctx.setLineDash([]);

            this.ctx.strokeStyle = isSelected ? this.colors.selected : '#2980b9';
            this.ctx.lineWidth = isSelected ? 3 / this.scale : 1 / this.scale;
            this.ctx.strokeRect(aisle.x, aisle.y, aisle.width, aisle.height);

            const centerX = aisle.x + aisle.width / 2;
            const centerY = aisle.y + aisle.height / 2;
            this.drawFlippedText(
                aisle.zone,
                centerX,
                centerY,
                {
                    font: `bold ${Math.max(12, 14 / this.zoom) / this.scale}px Arial`,
                    fillStyle: '#fff',
                    textAlign: 'center',
                    textBaseline: 'middle'
                }
            );

            if (isSelected) {
                this.drawDimensions(aisle.x, aisle.y, aisle.width, aisle.height);
            }
            return;
        }

        if (aisle.type === 'EMPTY_FLOOR') {
            this.ctx.fillStyle = '#ecf0f1';
            this.ctx.globalAlpha = isSelected ? 0.9 : 0.7;
            this.ctx.fillRect(aisle.x, aisle.y, aisle.width, aisle.height);

            this.ctx.strokeStyle = '#bdc3c7';
            this.ctx.lineWidth = 0.5 / this.scale;
            this.ctx.setLineDash([2 / this.scale, 2 / this.scale]);
            this.ctx.globalAlpha = 0.5;

            const gridSize = 2;
            for (let x = aisle.x; x <= aisle.x + aisle.width; x += gridSize) {
                this.ctx.beginPath();
                this.ctx.moveTo(x, aisle.y);
                this.ctx.lineTo(x, aisle.y + aisle.height);
                this.ctx.stroke();
            }
            for (let y = aisle.y; y <= aisle.y + aisle.height; y += gridSize) {
                this.ctx.beginPath();
                this.ctx.moveTo(aisle.x, y);
                this.ctx.lineTo(aisle.x + aisle.width, y);
                this.ctx.stroke();
            }
            this.ctx.setLineDash([]);
            this.ctx.globalAlpha = 1;

            this.ctx.strokeStyle = isSelected ? this.colors.selected : '#95a5a6';
            this.ctx.lineWidth = isSelected ? 3 / this.scale : 1 / this.scale;
            this.ctx.strokeRect(aisle.x, aisle.y, aisle.width, aisle.height);

            const centerX = aisle.x + aisle.width / 2;
            const centerY = aisle.y + aisle.height / 2;
            this.drawFlippedText(
                aisle.zone,
                centerX,
                centerY,
                {
                    font: `bold ${Math.max(12, 14 / this.zoom) / this.scale}px Arial`,
                    fillStyle: '#7f8c8d',
                    textAlign: 'center',
                    textBaseline: 'middle'
                }
            );
            this.drawFlippedText(
                'Empty Floor',
                centerX,
                centerY - 15 / this.scale,
                {
                    font: `${10 / this.scale}px Arial`,
                    fillStyle: '#95a5a6',
                    textAlign: 'center',
                    textBaseline: 'middle'
                }
            );

            if (isSelected) {
                this.drawDimensions(aisle.x, aisle.y, aisle.width, aisle.height);
            }
            return;
        }

        this.ctx.fillStyle = aisle.type === 'WORKING_STATUS'
            ? (aisle.workingStatusColor || this.colors[aisle.type] || this.colors['HIGH_RACK'])
            : (this.colors[aisle.type] || this.colors['HIGH_RACK']);
        this.ctx.globalAlpha = isSelected ? 0.8 : 0.5;
        this.ctx.fillRect(aisle.x, aisle.y, aisle.width, aisle.height);

        this.ctx.strokeStyle = isSelected ? this.colors.selected : this.colors.boundary;
        this.ctx.lineWidth = isSelected ? 3 / this.scale : 1 / this.scale;
        this.ctx.globalAlpha = 1;
        this.ctx.strokeRect(aisle.x, aisle.y, aisle.width, aisle.height);

        const centerX = aisle.x + aisle.width / 2;
        const centerY = aisle.y + aisle.height / 2;

        this.drawFlippedText(
            aisle.zone,
            centerX,
            centerY,
            {
                font: `bold ${Math.max(12, 14 / this.zoom) / this.scale}px Arial`,
                fillStyle: '#fff',
                textAlign: 'center',
                textBaseline: 'middle'
            }
        );

        this.drawFlippedText(
            aisle.type === 'WORKING_STATUS'
                ? (aisle.workingStatusLabel || 'Working Status')
                : `${aisle.locations.length} locations`,
            centerX,
            centerY - 15 / this.scale,
            {
                font: `${10 / this.scale}px Arial`,
                fillStyle: 'rgba(255,255,255,0.8)',
                textAlign: 'center',
                textBaseline: 'middle'
            }
        );

        if (isSelected) {
            this.drawDimensions(aisle.x, aisle.y, aisle.width, aisle.height);
        }
    }

    drawDockDoorArea(aisle, isSelected) {
        const edge = this.getNearestPerimeterEdge(aisle);
        const doorThickness = Math.max(0.2, Math.min(aisle.width, aisle.height) * 0.18);
        const thresholdThickness = Math.max(0.25, Math.min(aisle.width, aisle.height) * 0.24);

        let doorRect;
        let thresholdRect;
        switch (edge) {
            case 'left':
                doorRect = { x: aisle.x, y: aisle.y, width: doorThickness, height: aisle.height };
                thresholdRect = { x: aisle.x + doorThickness, y: aisle.y, width: thresholdThickness, height: aisle.height };
                break;
            case 'right':
                doorRect = { x: aisle.x + aisle.width - doorThickness, y: aisle.y, width: doorThickness, height: aisle.height };
                thresholdRect = { x: aisle.x + aisle.width - doorThickness - thresholdThickness, y: aisle.y, width: thresholdThickness, height: aisle.height };
                break;
            case 'bottom':
                doorRect = { x: aisle.x, y: aisle.y, width: aisle.width, height: doorThickness };
                thresholdRect = { x: aisle.x, y: aisle.y + doorThickness, width: aisle.width, height: thresholdThickness };
                break;
            case 'top':
            default:
                doorRect = { x: aisle.x, y: aisle.y + aisle.height - doorThickness, width: aisle.width, height: doorThickness };
                thresholdRect = { x: aisle.x, y: aisle.y + aisle.height - doorThickness - thresholdThickness, width: aisle.width, height: thresholdThickness };
                break;
        }

        this.ctx.fillStyle = '#d6eaf8';
        this.ctx.globalAlpha = isSelected ? 0.85 : 0.65;
        this.ctx.fillRect(aisle.x, aisle.y, aisle.width, aisle.height);

        this.ctx.fillStyle = this.colors.DOCK_DOOR;
        this.ctx.globalAlpha = 0.35;
        this.ctx.fillRect(aisle.x, aisle.y, aisle.width, aisle.height);

        this.ctx.fillStyle = '#34495e';
        this.ctx.globalAlpha = 0.95;
        this.ctx.fillRect(doorRect.x, doorRect.y, doorRect.width, doorRect.height);

        this.ctx.strokeStyle = '#aeb6bf';
        this.ctx.lineWidth = 0.8 / this.scale;
        const shutterStep = Math.max(0.22, Math.min(doorRect.width, doorRect.height) / 6);
        if (doorRect.width >= doorRect.height) {
            for (let y = doorRect.y + shutterStep; y < doorRect.y + doorRect.height; y += shutterStep) {
                this.ctx.beginPath();
                this.ctx.moveTo(doorRect.x, y);
                this.ctx.lineTo(doorRect.x + doorRect.width, y);
                this.ctx.stroke();
            }
        } else {
            for (let x = doorRect.x + shutterStep; x < doorRect.x + doorRect.width; x += shutterStep) {
                this.ctx.beginPath();
                this.ctx.moveTo(x, doorRect.y);
                this.ctx.lineTo(x, doorRect.y + doorRect.height);
                this.ctx.stroke();
            }
        }

        this.ctx.save();
        this.ctx.beginPath();
        this.ctx.rect(thresholdRect.x, thresholdRect.y, thresholdRect.width, thresholdRect.height);
        this.ctx.clip();
        this.ctx.fillStyle = '#f4d03f';
        this.ctx.globalAlpha = 0.9;
        this.ctx.fillRect(thresholdRect.x, thresholdRect.y, thresholdRect.width, thresholdRect.height);
        this.ctx.strokeStyle = 'rgba(44, 62, 80, 0.48)';
        this.ctx.lineWidth = 1 / this.scale;
        const stripeStep = Math.max(0.28, Math.min(thresholdRect.width, thresholdRect.height) / 3);
        for (let x = thresholdRect.x - thresholdRect.height; x <= thresholdRect.x + thresholdRect.width + thresholdRect.height; x += stripeStep) {
            this.ctx.beginPath();
            this.ctx.moveTo(x, thresholdRect.y);
            this.ctx.lineTo(x + thresholdRect.height, thresholdRect.y + thresholdRect.height);
            this.ctx.stroke();
        }
        this.ctx.restore();

        this.ctx.strokeStyle = 'rgba(255,255,255,0.45)';
        this.ctx.lineWidth = 0.9 / this.scale;
        this.ctx.setLineDash([4 / this.scale, 3 / this.scale]);
        if (edge === 'left' || edge === 'right') {
            const guideY1 = aisle.y + aisle.height * 0.32;
            const guideY2 = aisle.y + aisle.height * 0.68;
            const startX = edge === 'left' ? thresholdRect.x + thresholdRect.width : thresholdRect.x;
            const endX = edge === 'left' ? aisle.x + aisle.width : aisle.x;
            [guideY1, guideY2].forEach((guideY) => {
                this.ctx.beginPath();
                this.ctx.moveTo(startX, guideY);
                this.ctx.lineTo(endX, guideY);
                this.ctx.stroke();
            });
        } else {
            const guideX1 = aisle.x + aisle.width * 0.32;
            const guideX2 = aisle.x + aisle.width * 0.68;
            const startY = edge === 'bottom' ? thresholdRect.y + thresholdRect.height : thresholdRect.y;
            const endY = edge === 'bottom' ? aisle.y + aisle.height : aisle.y;
            [guideX1, guideX2].forEach((guideX) => {
                this.ctx.beginPath();
                this.ctx.moveTo(guideX, startY);
                this.ctx.lineTo(guideX, endY);
                this.ctx.stroke();
            });
        }
        this.ctx.setLineDash([]);

        this.ctx.globalAlpha = 1;
        this.ctx.strokeStyle = isSelected ? this.colors.selected : '#2874a6';
        this.ctx.lineWidth = isSelected ? 3 / this.scale : 1.5 / this.scale;
        this.ctx.strokeRect(aisle.x, aisle.y, aisle.width, aisle.height);

        const centerX = aisle.x + aisle.width / 2;
        const centerY = aisle.y + aisle.height / 2;
        this.drawFlippedText(
            aisle.zone,
            centerX,
            centerY + 8 / this.scale,
            {
                font: `bold ${Math.max(11, 13 / this.zoom) / this.scale}px Arial`,
                fillStyle: '#ffffff',
                textAlign: 'center',
                textBaseline: 'middle'
            }
        );

        this.drawFlippedText(
            'Dock Door',
            centerX,
            centerY - 8 / this.scale,
            {
                font: `${10 / this.scale}px Arial`,
                fillStyle: '#ecf0f1',
                textAlign: 'center',
                textBaseline: 'middle'
            }
        );
    }

    drawLocationBoxes(aisle) {
        if (!this.isStorageType(aisle.type)) {
            return;
        }

        const dims = this.getLocationDimensions(aisle.type);
        const uniqueLocations = new Map();

        // Group locations by position (not level)
        aisle.locations.forEach(loc => {
            const key = `${loc.x}-${loc.y}`;
            if (!uniqueLocations.has(key)) {
                uniqueLocations.set(key, { ...loc, maxLevel: loc.level });
            } else {
                const existing = uniqueLocations.get(key);
                if (loc.level > existing.maxLevel) {
                    existing.maxLevel = loc.level;
                }
            }
        });

        // Draw aisle spaces for floor storage and staging
        if (aisle.type === 'FLOOR' || aisle.type === 'STAGING') {
            const rowDepth = aisle.bayDepth || dims.depth;
            const aisleWidth = aisle.aisleWidth || dims.aisleWidth || 1.5;
            const rowPlusAisle = rowDepth + aisleWidth;
            const numRows = Math.max(1, Math.floor(aisle.width / rowPlusAisle));

            // Draw aisle markings
            this.ctx.fillStyle = '#e8e8e8';
            this.ctx.strokeStyle = '#aaa';
            this.ctx.setLineDash([3 / this.scale, 3 / this.scale]);

            for (let row = 0; row < numRows - 1; row++) {
                const aisleX = aisle.x + (row * rowPlusAisle) + rowDepth;
                this.ctx.globalAlpha = 0.5;
                this.ctx.fillRect(aisleX, aisle.y, aisleWidth, aisle.height);
                this.ctx.globalAlpha = 1;
                this.ctx.strokeRect(aisleX, aisle.y, aisleWidth, aisle.height);

                // Draw walking direction arrow
                if (this.zoom > 0.5) {
                    this.ctx.fillStyle = '#999';
                    this.ctx.font = `${10 / this.scale}px Arial`;
                    this.ctx.textAlign = 'center';
                    this.ctx.textBaseline = 'middle';
                    this.drawFlippedText('↕', aisleX + aisleWidth / 2, aisle.y + aisle.height / 2);
                }
            }
            this.ctx.setLineDash([]);
        }

        // Draw back-to-back aisle for racks
        if (aisle.type === 'HIGH_RACK' || aisle.type === 'SHELF') {
            const rowDepth = aisle.bayDepth || dims.depth;
            const aisleWidth = aisle.aisleWidth || dims.aisleWidth || 2.5;
            const rowWidth = (2 * rowDepth) + aisleWidth;
            const numRows = Math.floor(aisle.width / rowWidth);

            // Draw aisle markings between back-to-back rows
            this.ctx.fillStyle = '#f0f0f0';
            this.ctx.strokeStyle = '#bbb';
            this.ctx.setLineDash([5 / this.scale, 3 / this.scale]);

            for (let row = 0; row < numRows; row++) {
                const aisleX = aisle.x + (row * rowWidth) + rowDepth;
                this.ctx.globalAlpha = 0.4;
                this.ctx.fillRect(aisleX, aisle.y, aisleWidth, aisle.height);
                this.ctx.globalAlpha = 1;
                this.ctx.strokeRect(aisleX, aisle.y, aisleWidth, aisle.height);
            }
            this.ctx.setLineDash([]);
        }

        uniqueLocations.forEach((loc, key) => {
            let fillColor;

            // Check if location has a slotted product (from slotting module)
            if (loc.slottedClass) {
                // Use velocity class colors
                switch (loc.slottedClass) {
                    case 'A':
                        fillColor = 'rgba(231, 76, 60, 0.8)';  // Red for A-class (high velocity)
                        break;
                    case 'B':
                        fillColor = 'rgba(243, 156, 18, 0.8)'; // Orange for B-class (medium)
                        break;
                    case 'C':
                        fillColor = 'rgba(52, 152, 219, 0.8)'; // Blue for C-class (low velocity)
                        break;
                    default:
                        fillColor = this.colors[aisle.type] || this.colors['HIGH_RACK'];
                }
            } else if (this.showHeatmap) {
                // Heatmap by level
                const levelIndex = Math.min(loc.maxLevel - 1, this.colors.levelColors.length - 1);
                fillColor = this.colors.levelColors[levelIndex];
            } else {
                fillColor = this.colors[aisle.type] || this.colors['HIGH_RACK'];
            }

            // Determine box dimensions based on type
            let boxWidth, boxDepth;
            if (aisle.type === 'FLOOR' || aisle.type === 'STAGING') {
                boxWidth = (aisle.bayDepth || dims.depth) * 0.9;
                boxDepth = (aisle.bayWidth || dims.width) * 0.9;
            } else {
                boxWidth = dims.width * 0.9;
                boxDepth = dims.depth * 0.9;
            }

            // Draw location box
            this.ctx.fillStyle = fillColor;
            this.ctx.globalAlpha = 0.7;
            this.ctx.fillRect(loc.x, loc.y, boxWidth, boxDepth);

            // Border
            this.ctx.strokeStyle = this.colors.boundary;
            this.ctx.lineWidth = 0.5 / this.scale;
            this.ctx.globalAlpha = 1;
            this.ctx.strokeRect(loc.x, loc.y, boxWidth, boxDepth);

            // Label if zoomed in enough and labels enabled
            if (this.showLabels && this.zoom > 1.5) {
                this.ctx.fillStyle = '#000';
                this.ctx.font = `${8 / this.scale}px Arial`;
                this.ctx.textAlign = 'center';
                this.ctx.textBaseline = 'middle';

                const label = loc.location.split('-').slice(-2).join('-');
                this.drawFlippedText(
                    label,
                    loc.x + boxWidth / 2,
                    loc.y + boxDepth / 2
                );
            }
        });

        this.ctx.globalAlpha = 1;
    }

    drawConstraintArea(aisle, isSelected) {
        const fill = this.colors[aisle.type] || this.colors.OBSTACLE;

        this.ctx.fillStyle = fill;
        this.ctx.globalAlpha = aisle.type === 'SAFETY_BUFFER' ? 0.18 : 0.45;
        this.ctx.fillRect(aisle.x, aisle.y, aisle.width, aisle.height);

        this.ctx.save();
        this.ctx.beginPath();
        this.ctx.rect(aisle.x, aisle.y, aisle.width, aisle.height);
        this.ctx.clip();

        this.ctx.strokeStyle = 'rgba(0,0,0,0.25)';
        this.ctx.lineWidth = 1 / this.scale;
        const step = 1.2;
        for (let x = aisle.x - aisle.height; x <= aisle.x + aisle.width + aisle.height; x += step) {
            this.ctx.beginPath();
            this.ctx.moveTo(x, aisle.y);
            this.ctx.lineTo(x + aisle.height, aisle.y + aisle.height);
            this.ctx.stroke();
        }
        this.ctx.restore();

        this.ctx.globalAlpha = 1;
        this.ctx.strokeStyle = isSelected ? this.colors.selected : '#2c3e50';
        this.ctx.lineWidth = isSelected ? 3 / this.scale : 1.5 / this.scale;
        this.ctx.strokeRect(aisle.x, aisle.y, aisle.width, aisle.height);

        const centerX = aisle.x + aisle.width / 2;
        const centerY = aisle.y + aisle.height / 2;
        this.drawFlippedText(
            aisle.zone,
            centerX,
            centerY + 8 / this.scale,
            {
                font: `bold ${Math.max(11, 13 / this.zoom) / this.scale}px Arial`,
                fillStyle: '#ffffff',
                textAlign: 'center',
                textBaseline: 'middle'
            }
        );

        this.drawFlippedText(
            aisle.type.replaceAll('_', ' '),
            centerX,
            centerY - 8 / this.scale,
            {
                font: `${10 / this.scale}px Arial`,
                fillStyle: '#ecf0f1',
                textAlign: 'center',
                textBaseline: 'middle'
            }
        );
    }

    addBoundaryPoint(x, y) {
        if (x < 0 || x > this.warehouseWidth || y < 0 || y > this.warehouseHeight) {
            return;
        }

        const point = { x, y };
        if (this.boundaryDraftPoints.length >= 3) {
            const first = this.boundaryDraftPoints[0];
            const distanceToFirst = Math.hypot(point.x - first.x, point.y - first.y);
            if (distanceToFirst <= this.boundaryCloseDistance) {
                this.finalizeBoundaryDraft();
                return;
            }
        }

        this.boundaryDraftPoints.push(point);
        this.render();
    }

    cancelBoundaryDraft() {
        this.boundaryDraftPoints = [];
        this.boundaryHoverPoint = null;
    }

    finalizeBoundaryDraft() {
        if (this.boundaryDraftPoints.length < 3) {
            return;
        }

        this.boundaryPolygon = this.boundaryDraftPoints.map(p => ({ x: p.x, y: p.y }));
        this.cancelBoundaryDraft();
        this.applyConstraintFiltering();
        this.saveState();
        if (this.onAislesChanged) {
            this.onAislesChanged();
        }
        this.render();
    }

    drawBoundaryDraft() {
        if (this.boundaryDraftPoints.length === 0) return;

        this.ctx.strokeStyle = '#16a085';
        this.ctx.lineWidth = 2 / this.scale;
        this.ctx.setLineDash([6 / this.scale, 4 / this.scale]);
        this.ctx.beginPath();
        this.ctx.moveTo(this.boundaryDraftPoints[0].x, this.boundaryDraftPoints[0].y);
        for (let i = 1; i < this.boundaryDraftPoints.length; i++) {
            this.ctx.lineTo(this.boundaryDraftPoints[i].x, this.boundaryDraftPoints[i].y);
        }
        if (this.boundaryHoverPoint) {
            this.ctx.lineTo(this.boundaryHoverPoint.x, this.boundaryHoverPoint.y);
        }
        this.ctx.stroke();
        this.ctx.setLineDash([]);

        this.boundaryDraftPoints.forEach((p, idx) => {
            this.ctx.fillStyle = idx === 0 ? '#e67e22' : '#16a085';
            this.ctx.beginPath();
            this.ctx.arc(p.x, p.y, 0.35, 0, Math.PI * 2);
            this.ctx.fill();
        });
    }

    drawDrawingPreview() {
        if (!this.isDrawing) return;

        const width = Math.abs(this.currentX - this.startX);
        const height = Math.abs(this.currentY - this.startY);

        if (width < 1 || height < 1) return;

        const x = Math.min(this.startX, this.currentX);
        const y = Math.min(this.startY, this.currentY);

        // Preview rectangle
        this.ctx.fillStyle = this.colors.preview;
        this.ctx.strokeStyle = this.colors[this.defaultType];
        this.ctx.lineWidth = 2 / this.scale;
        this.ctx.fillRect(x, y, width, height);
        this.ctx.strokeRect(x, y, width, height);

        // Dimensions
        this.drawDimensions(x, y, width, height);

        // Preview location count
        if (!this.isStorageType(this.defaultType)) {
            this.ctx.fillStyle = this.colors.text;
            this.ctx.font = `${12 / this.scale}px Arial`;
            this.ctx.textAlign = 'center';
            this.drawFlippedText(
                `${this.defaultType.replaceAll('_', ' ')}`,
                x + width / 2,
                y + height / 2
            );
            return;
        }

        const dims = this.getLocationDimensions(this.defaultType);
        const rowWidth = (2 * dims.depth) + dims.aisleWidth;
        const numRows = Math.floor(width / rowWidth);
        const positionsPerRow = Math.floor(height / dims.width);
        const estimatedLocations = numRows * positionsPerRow * 2 * 4; // Assume 4 levels

        this.ctx.fillStyle = this.colors.text;
        this.ctx.font = `${12 / this.scale}px Arial`;
        this.ctx.textAlign = 'center';
        this.drawFlippedText(
            `~${estimatedLocations} locations`,
            x + width / 2,
            y + height / 2
        );
    }

    drawDimensions(x, y, width, height) {
        const fontSize = 12 / this.scale;

        // Background for text
        const widthText = `${width.toFixed(1)}m`;
        const heightText = `${height.toFixed(1)}m`;

        // Width dimension (at top of aisle)
        this.drawFlippedText(
            widthText,
            x + width / 2,
            y + height + 12 / this.scale,
            { font: `bold ${fontSize}px Arial`, fillStyle: this.colors.dimension, textAlign: 'center', textBaseline: 'middle' }
        );

        // Height dimension (left side)
        this.ctx.save();
        this.ctx.translate(x - 12 / this.scale, y + height / 2);
        this.ctx.scale(1, -1);
        this.ctx.rotate(Math.PI / 2);
        this.ctx.font = `bold ${fontSize}px Arial`;
        this.ctx.fillStyle = this.colors.dimension;
        this.ctx.textAlign = 'center';
        this.ctx.fillText(heightText, 0, 0);
        this.ctx.restore();
    }

    drawMeasurement() {
        if (!this.measureStart || !this.measureEnd) return;

        const dx = this.measureEnd.x - this.measureStart.x;
        const dy = this.measureEnd.y - this.measureStart.y;
        const distance = Math.sqrt(dx * dx + dy * dy);

        // Draw line
        this.ctx.strokeStyle = this.colors.measure;
        this.ctx.lineWidth = 2 / this.scale;
        this.ctx.setLineDash([5 / this.scale, 5 / this.scale]);
        this.ctx.beginPath();
        this.ctx.moveTo(this.measureStart.x, this.measureStart.y);
        this.ctx.lineTo(this.measureEnd.x, this.measureEnd.y);
        this.ctx.stroke();
        this.ctx.setLineDash([]);

        // Draw endpoints
        this.ctx.fillStyle = this.colors.measure;
        this.ctx.beginPath();
        this.ctx.arc(this.measureStart.x, this.measureStart.y, 4 / this.scale, 0, Math.PI * 2);
        this.ctx.fill();
        this.ctx.beginPath();
        this.ctx.arc(this.measureEnd.x, this.measureEnd.y, 4 / this.scale, 0, Math.PI * 2);
        this.ctx.fill();

        // Draw distance label
        const midX = (this.measureStart.x + this.measureEnd.x) / 2;
        const midY = (this.measureStart.y + this.measureEnd.y) / 2;

        // Background rect (flip for proper orientation)
        this.ctx.save();
        this.ctx.translate(midX, midY);
        this.ctx.scale(1, -1);
        this.ctx.fillStyle = 'white';
        this.ctx.fillRect(-25 / this.scale, -10 / this.scale, 50 / this.scale, 20 / this.scale);
        this.ctx.restore();

        // Distance text
        this.drawFlippedText(
            `${distance.toFixed(2)}m`,
            midX,
            midY,
            { font: `bold ${12 / this.scale}px Arial`, fillStyle: this.colors.measure, textAlign: 'center', textBaseline: 'middle' }
        );
    }

    drawInstructions() {
        this.ctx.fillStyle = this.colors.text;
        this.ctx.font = '12px Arial';
        this.ctx.textAlign = 'left';

        const modeText = {
            'draw': 'Draw',
            'select': 'Select',
            'delete': 'Delete',
            'measure': 'Measure',
            'move': 'Move',
            'boundary': 'Boundary Polygon'
        }[this._mode] || 'Draw';

        // Show selection count if multiple aisles selected
        const selectionInfo = this.selectedAisles.length > 1
            ? ` | ${this.selectedAisles.length} aisles selected`
            : this.selectedAisle
                ? ` | 1 aisle selected`
                : '';

        const instructions = [
            `Mode: ${modeText} | Zoom: ${Math.round(this.zoom * 100)}%${selectionInfo}`,
            this._mode === 'boundary'
                ? 'Click to add points | Click near first point or press Enter to close | Delete clears boundary'
                : this._mode === 'move' && this.selectedAisles.length > 0
                    ? 'Scroll: Rotate selection | Ctrl+Scroll: Zoom | Ctrl+Click: Multi-select | Middle-click/Space+drag: Pan'
                    : 'Ctrl+Click: Multi-select | Ctrl+A: Select all | Scroll: Zoom | Middle-click/Space+drag: Pan'
        ];

        instructions.forEach((instruction, index) => {
            this.ctx.fillText(instruction, 10, this.canvas.height - 20 + index * 15);
        });
    }

    // Export methods
    toJSON() {
        return {
            warehouseWidth: this.warehouseWidth,
            warehouseHeight: this.warehouseHeight,
            boundaryPolygon: this.boundaryPolygon ? this.boundaryPolygon.map(p => ({ ...p })) : null,
            aisles: this.aisles.map(a => ({
                id: a.id,
                x: a.x,
                y: a.y,
                width: a.width,
                height: a.height,
                type: a.type,
                direction: a.direction || 'AUTO',
                workingStatusFlow: a.workingStatusFlow || null,
                workingStatusCode: a.workingStatusCode || null,
                workingStatusLabel: a.workingStatusLabel || null,
                workingStatusColor: a.workingStatusColor || null,
                levels: a.levels,
                zone: a.zone,
                bayWidth: a.bayWidth,
                bayDepth: a.bayDepth,
                aisleWidth: a.aisleWidth,
                pickFaceLevels: a.pickFaceLevels || null,
                tunnelLevelFrom: a.tunnelLevelFrom || 1,
                tunnelLevelTo: a.tunnelLevelTo || a.levels || 1,
                startPointX: a.startPointX,
                startPointY: a.startPointY,
                baseLocations: a.baseLocations || a.locations,
                locations: a.locations
            }))
        };
    }

    fromJSON(data) {
        this.warehouseWidth = data.warehouseWidth;
        this.warehouseHeight = data.warehouseHeight;
        this.boundaryPolygon = Array.isArray(data.boundaryPolygon) ? data.boundaryPolygon.map(p => ({ ...p })) : null;
        this.aisles = data.aisles.map(a => {
            const aisle = new Aisle(a.id, a.x, a.y, a.width, a.height, a.type, a.levels, a.zone);
            aisle.direction = a.direction || 'AUTO';
            aisle.workingStatusFlow = a.workingStatusFlow || null;
            aisle.workingStatusCode = a.workingStatusCode || null;
            aisle.workingStatusLabel = a.workingStatusLabel || null;
            aisle.workingStatusColor = a.workingStatusColor || null;
            aisle.bayWidth = a.bayWidth;
            aisle.bayDepth = a.bayDepth;
            aisle.aisleWidth = a.aisleWidth;
            aisle.pickFaceLevels = Array.isArray(a.pickFaceLevels) ? [...a.pickFaceLevels] : undefined;
            aisle.tunnelLevelFrom = a.tunnelLevelFrom || 1;
            aisle.tunnelLevelTo = a.tunnelLevelTo || a.levels || 1;
            aisle.startPointX = a.startPointX;
            aisle.startPointY = a.startPointY;
            aisle.tunnelRules = [];
            aisle.baseLocations = a.baseLocations || a.locations || [];
            aisle.locations = a.locations || [];
            return aisle;
        });
        this.nextAisleId = Math.max(...this.aisles.map(a => a.id), 0) + 1;
        this.applyConstraintFiltering();
        this.setupCanvas();
        this.render();

        if (this.onAislesChanged) {
            this.onAislesChanged();
        }
    }

    // =====================================================
    // STOCK & INVENTORY MANAGEMENT
    // =====================================================

    /**
     * Get all locations across all aisles
     */
    getAllLocations() {
        const locations = [];
        for (const aisle of this.aisles) {
            for (const loc of aisle.locations) {
                locations.push({
                    ...loc,
                    aisleId: aisle.id,
                    aisleZone: aisle.zone
                });
            }
        }
        return locations;
    }

    /**
     * Initialize stock levels for all locations
     * @param {string} mode - 'random' or 'fixed'
     * @param {number} maxQty - Maximum quantity (for random) or fixed quantity
     * @param {number} minThreshold - Minimum stock threshold for alerts
     */
    initializeStock(mode = 'random', maxQty = 100, minThreshold = 10) {
        for (const aisle of this.aisles) {
            for (const loc of aisle.locations) {
                if (mode === 'random') {
                    loc.stockQty = Math.floor(Math.random() * (maxQty + 1));
                } else {
                    loc.stockQty = maxQty;
                }
                loc.minThreshold = minThreshold;
                loc.maxCapacity = maxQty;
            }
        }
        this.render();
    }

    /**
     * Load stock from CSV/JSON data
     * @param {Array} stockData - Array of {location, stockQty, minThreshold}
     */
    loadStock(stockData) {
        const stockMap = new Map();
        for (const item of stockData) {
            stockMap.set(item.location, {
                stockQty: parseInt(item.stockQty) || 0,
                minThreshold: parseInt(item.minThreshold) || 10
            });
        }

        for (const aisle of this.aisles) {
            for (const loc of aisle.locations) {
                const stock = stockMap.get(loc.location);
                if (stock) {
                    loc.stockQty = stock.stockQty;
                    loc.minThreshold = stock.minThreshold;
                }
            }
        }
        this.render();
    }

    /**
     * Decrement stock at a location
     * @param {string} locationName - Location identifier
     * @param {number} qty - Quantity to decrement
     * @returns {Object|null} - Replenishment alert if below threshold
     */
    decrementStock(locationName, qty = 1) {
        for (const aisle of this.aisles) {
            for (const loc of aisle.locations) {
                if (loc.location === locationName) {
                    const prevQty = loc.stockQty || 0;
                    loc.stockQty = Math.max(0, prevQty - qty);

                    // Check if replenishment needed
                    if (loc.stockQty <= (loc.minThreshold || 10) && prevQty > (loc.minThreshold || 10)) {
                        return {
                            location: locationName,
                            currentQty: loc.stockQty,
                            threshold: loc.minThreshold || 10,
                            needsReplenishment: true
                        };
                    }
                    return null;
                }
            }
        }
        return null;
    }

    /**
     * Get stock level color for visualization (green -> yellow -> red)
     * @param {Object} location - Location object with stockQty and maxCapacity
     */
    getStockColor(location) {
        if (!location.stockQty || !location.maxCapacity) return null;

        const ratio = location.stockQty / location.maxCapacity;
        if (ratio > 0.6) return 'rgba(46, 204, 113, 0.6)'; // Green
        if (ratio > 0.3) return 'rgba(241, 196, 15, 0.6)'; // Yellow
        if (ratio > 0) return 'rgba(231, 76, 60, 0.6)'; // Red
        return 'rgba(149, 165, 166, 0.6)'; // Empty - Gray
    }
}

/**
 * Aisle class - Represents a single storage aisle/zone
 */
class Aisle {
    constructor(id, x, y, width, height, type, levels, zone) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.type = type;
        this.levels = levels;
        this.zone = zone;
        this.locations = [];

        // Configurable dimensions
        this.bayWidth = this.getDefaultDimensions().width;
        this.bayDepth = this.getDefaultDimensions().depth;
        this.aisleWidth = this.getDefaultDimensions().aisleWidth;

        // Naming configuration (optional, uses defaults if not set)
        this.namingConfig = null;
        this.direction = 'AUTO';

        // Constraint metadata (v1)
        this.tunnelLevelFrom = 1;
        this.tunnelLevelTo = Math.max(1, levels || 1);
        this.tunnelRules = [];
        this.baseLocations = [];
        this.workingStatusFlow = null;
        this.workingStatusCode = null;
        this.workingStatusLabel = null;
        this.workingStatusColor = null;

        if (this.type === 'WORKING_STATUS') {
            this.workingStatusFlow = 'OUTBOUND';
            this.workingStatusCode = '300';
            this.workingStatusLabel = 'Allocated';
            this.workingStatusColor = '#1A005D';
        }
    }

    getDefaultDimensions() {
        const defaults = {
            'HIGH_RACK': { width: 1.2, depth: 1.0, aisleWidth: 2.5 },
            'SHELF': { width: 1.0, depth: 0.6, aisleWidth: 1.8 },
            'DRIVE_IN': { width: 1.2, depth: 1.2, aisleWidth: 0 },
            'FLOOR': { width: 1.2, depth: 1.0, aisleWidth: 1.5 },      // Min 1.5m aisle for walking
            'STAGING': { width: 1.2, depth: 1.0, aisleWidth: 2.0 },     // Wider aisle for staging operations
            'WORKING_STATUS': { width: 1.2, depth: 1.0, aisleWidth: 0 },
            'DOCK_DOOR': { width: 3.2, depth: 1.4, aisleWidth: 0 }
        };
        return defaults[this.type] || defaults['HIGH_RACK'];
    }

    // Format location name based on naming configuration
    formatLocationName(row, bay, side, level) {
        const cfg = this.namingConfig || {};
        const pattern = cfg.pattern || 'zone-row-bay-side-level';
        const rowPadding = cfg.rowPadding || 2;
        const bayPadding = cfg.bayPadding || 3;
        const levelPrefix = cfg.levelPrefix || 'L';
        const customPattern = cfg.customPattern || '{zone}-{row}-{bay}{side}-{level}';
        const rowFormat = cfg.rowFormat || 'numeric';  // 'numeric', 'alpha', 'alpha2'

        // Get bay numbering type for this side
        const leftBayNumbering = cfg.leftBayNumbering || 'odd';
        const rightBayNumbering = cfg.rightBayNumbering || 'even';

        // Calculate bay number based on side and numbering type
        let bayNum;
        const baseBay = (cfg.bayFrom || 1) + bay - 1;

        if (side === 'L') {
            switch (leftBayNumbering) {
                case 'odd':
                    bayNum = (bay - 1) * 2 + 1 + (cfg.bayFrom || 1) - 1;
                    break;
                case 'even':
                    bayNum = bay * 2 + (cfg.bayFrom || 1) - 1;
                    break;
                case 'running':
                default:
                    bayNum = baseBay;
                    break;
            }
        } else if (side === 'R') {
            switch (rightBayNumbering) {
                case 'odd':
                    bayNum = (bay - 1) * 2 + 1 + (cfg.bayFrom || 1) - 1;
                    break;
                case 'even':
                    bayNum = bay * 2 + (cfg.bayFrom || 1) - 1;
                    break;
                case 'running':
                default:
                    bayNum = baseBay;
                    break;
            }
        } else {
            // No side (FLOOR, STAGING, DRIVE_IN)
            bayNum = baseBay;
        }

        // Convert row number to string based on format
        let rowStr;
        switch (rowFormat) {
            case 'alpha':
                // A, B, C, ... Z, AA, AB, AC...
                rowStr = this.numberToAlpha(row);
                break;
            case 'alpha2':
                // AA, AB, AC, ... AZ, BA, BB, BC...
                rowStr = this.numberToAlpha2(row);
                break;
            case 'numeric':
            default:
                rowStr = String(row).padStart(rowPadding, '0');
                break;
        }

        const bayStr = String(bayNum).padStart(bayPadding, '0');
        const levelStr = `${levelPrefix}${level}`;
        const sideStr = side || '';

        switch (pattern) {
            case 'zone-row-bay-level':
                return `${this.zone}-${rowStr}-${bayStr}-${levelStr}`;
            case 'zone-bay-level':
                return `${this.zone}-${bayStr}-${levelStr}`;
            case 'zone-row-bay-side-level':
                return `${this.zone}-${rowStr}-${bayStr}${sideStr}-${levelStr}`;
            case 'custom':
                return customPattern
                    .replace('{zone}', this.zone)
                    .replace('{row}', rowStr)
                    .replace('{bay}', bayStr)
                    .replace('{side}', sideStr)
                    .replace('{level}', levelStr);
            default:
                return `${this.zone}-${rowStr}-${bayStr}${sideStr}-${levelStr}`;
        }
    }

    // Convert number to single letter (1=A, 2=B, ..., 26=Z, 27=AA, 28=AB, ...)
    numberToAlpha(num) {
        let result = '';
        while (num > 0) {
            num--;
            result = String.fromCharCode(65 + (num % 26)) + result;
            num = Math.floor(num / 26);
        }
        return result || 'A';
    }

    // Convert number to double letter format (1=AA, 2=AB, ..., 26=AZ, 27=BA, ...)
    numberToAlpha2(num) {
        const first = Math.floor((num - 1) / 26);
        const second = (num - 1) % 26;
        return String.fromCharCode(65 + first) + String.fromCharCode(65 + second);
    }

    containsPoint(px, py) {
        return px >= this.x && px <= this.x + this.width &&
            py >= this.y && py <= this.y + this.height;
    }

    updateProperties(props) {
        if (props.zone !== undefined) this.zone = props.zone;
        if (props.type !== undefined) this.type = props.type;
        if (props.direction !== undefined) {
            const desiredDirection = String(props.direction || 'AUTO').toUpperCase();
            if (desiredDirection !== 'AUTO' && desiredDirection !== this.getResolvedDirection()) {
                this.rotate();
            }
            this.direction = desiredDirection;
        }
        if (props.levels !== undefined) this.levels = props.levels;
        if (props.bayWidth !== undefined) this.bayWidth = props.bayWidth;
        if (props.bayDepth !== undefined) this.bayDepth = props.bayDepth;
        if (props.aisleWidth !== undefined) this.aisleWidth = props.aisleWidth;
        if (props.pickFaceLevels !== undefined) this.pickFaceLevels = props.pickFaceLevels;
        if (props.startPointX !== undefined) this.startPointX = props.startPointX;
        if (props.startPointY !== undefined) this.startPointY = props.startPointY;
        if (props.workingStatusFlow !== undefined) this.workingStatusFlow = props.workingStatusFlow;
        if (props.workingStatusCode !== undefined) this.workingStatusCode = props.workingStatusCode;
        if (props.workingStatusLabel !== undefined) this.workingStatusLabel = props.workingStatusLabel;
        if (props.workingStatusColor !== undefined) this.workingStatusColor = props.workingStatusColor;
        if (props.tunnelLevelFrom !== undefined) this.tunnelLevelFrom = props.tunnelLevelFrom;
        if (props.tunnelLevelTo !== undefined) this.tunnelLevelTo = props.tunnelLevelTo;
        this.tunnelRules = [];

        const tunnelFrom = Math.max(1, Number(this.tunnelLevelFrom || 1));
        const tunnelTo = Math.max(1, Number(this.tunnelLevelTo || this.levels || 1));
        this.tunnelLevelFrom = Math.min(tunnelFrom, tunnelTo);
        this.tunnelLevelTo = Math.max(tunnelFrom, tunnelTo);
        if (this.type === 'TUNNEL_ZONE') {
            this.levels = Math.max(Number(this.levels || 1), this.tunnelLevelTo);
        }
        if (this.type === 'WORKING_STATUS' && !this.workingStatusCode) {
            this.workingStatusFlow = this.workingStatusFlow || 'OUTBOUND';
            this.workingStatusCode = '300';
            this.workingStatusLabel = this.workingStatusLabel || 'Allocated';
            this.workingStatusColor = this.workingStatusColor || '#1A005D';
        }

        this.generateLocations();
    }

    getResolvedDirection() {
        if (this.direction === 'HORIZONTAL' || this.direction === 'VERTICAL') {
            return this.direction;
        }
        return this.height >= this.width ? 'VERTICAL' : 'HORIZONTAL';
    }

    rotate(boundsWidth = null, boundsHeight = null) {
        const currentDirection = this.getResolvedDirection();
        const centerX = this.x + (this.width / 2);
        const centerY = this.y + (this.height / 2);
        const nextWidth = this.height;
        const nextHeight = this.width;

        this.width = nextWidth;
        this.height = nextHeight;
        this.x = centerX - (nextWidth / 2);
        this.y = centerY - (nextHeight / 2);

        if (Number.isFinite(boundsWidth) && Number.isFinite(boundsHeight)) {
            this.x = Math.max(0, Math.min(boundsWidth - this.width, this.x));
            this.y = Math.max(0, Math.min(boundsHeight - this.height, this.y));
        }

        this.direction = currentDirection === 'VERTICAL' ? 'HORIZONTAL' : 'VERTICAL';

        if (this.startPointX !== undefined) this.startPointX = this.x;
        if (this.startPointY !== undefined) this.startPointY = this.y;

        this.generateLocations();
    }

    isLevelBlockedByTunnel(side, position, level) {
        return false;
    }

    generateLocations() {
        this.locations = [];

        // Non-storage areas do not generate storage locations
        if (
            this.type === 'EMPTY_FLOOR' ||
            this.type === 'WALKWAY' ||
            this.type === 'WORKING_STATUS' ||
            this.type === 'OBSTACLE' ||
            this.type === 'PILLAR' ||
            this.type === 'TUNNEL_ZONE' ||
            this.type === 'WORKSTATION' ||
            this.type === 'MHE_PARKING' ||
            this.type === 'BATTERY_ROOM' ||
            this.type === 'SAFETY_BUFFER' ||
            this.type === 'OFFICE_AREA' ||
            this.type === 'DOCK_DOOR'
        ) {
            this.baseLocations = [];
            return;
        }

        const dims = {
            width: this.bayWidth,
            depth: this.bayDepth,
            aisleWidth: this.aisleWidth
        };

        // Get level range from config or use defaults
        const levelFrom = this.namingConfig?.levelFrom || 1;
        const levelTo = this.namingConfig?.levelTo || this.levels;
        const numLevels = levelTo - levelFrom + 1;

        // For drive-in racks, single side only
        if (this.type === 'DRIVE_IN') {
            const lanesPerRow = Math.floor(this.width / dims.width);
            const depth = Math.floor(this.height / dims.depth);

            for (let lane = 0; lane < lanesPerRow; lane++) {
                for (let pos = 0; pos < depth; pos++) {
                    const locX = this.x + (lane * dims.width);
                    const locY = this.y + (pos * dims.depth);

                    for (let lvl = 0; lvl < numLevels; lvl++) {
                        const level = levelFrom + lvl;
                        if (this.isLevelBlockedByTunnel('', pos + 1, level)) {
                            continue;
                        }
                        const locId = this.formatLocationName(lane + 1, pos + 1, '', level);
                        this.locations.push({
                            location: locId,
                            x: locX,
                            y: locY,
                            zone: this.zone,
                            type: this.type,
                            level: level,
                            aisle: lane + 1,
                            position: pos + 1
                        });
                    }
                }
            }
            return;
        }

        // For floor storage and staging - includes walking aisles
        if (this.type === 'FLOOR' || this.type === 'STAGING') {
            // Layout: [row of pallets] [aisle] [row of pallets] [aisle] ...
            // Each row is one pallet deep, then aisle for walking
            const rowDepth = dims.depth;  // Single pallet depth
            const rowPlusAisle = rowDepth + dims.aisleWidth;  // Row + walking aisle
            const numRows = Math.max(1, Math.floor(this.width / rowPlusAisle));
            const positionsPerRow = Math.floor(this.height / dims.width);

            for (let row = 0; row < numRows; row++) {
                const rowX = this.x + (row * rowPlusAisle);

                for (let pos = 0; pos < positionsPerRow; pos++) {
                    const locY = this.y + (pos * dims.width);
                    const locationNum = (row * positionsPerRow) + pos + 1;

                    const locId = this.formatLocationName(row + 1, pos + 1, '', levelFrom);
                    this.locations.push({
                        location: locId,
                        x: rowX,
                        y: locY,
                        zone: this.zone,
                        type: this.type,
                        level: 1,
                        aisle: row + 1,
                        position: pos + 1,
                        rowX: rowX,
                        aisleAfter: row < numRows - 1  // Has aisle after this row
                    });
                }
            }
            return;
        }

        // Standard back-to-back rack layout
        const rowWidth = (2 * dims.depth) + dims.aisleWidth;
        const numRows = Math.floor(this.width / rowWidth);
        const positionsPerRow = Math.floor(this.height / dims.width);

        // Get base row number from config (for when this aisle is part of a generated set)
        const baseRow = this.namingConfig?.currentRow || 1;

        let currentX = this.x;

        for (let row = 0; row < numRows; row++) {
            // Calculate the actual row number for this internal row
            // Each internal row gets its own number: baseRow, baseRow+1, etc.
            // Or if single zone, use L/R to distinguish sides of each aisle
            const aisleNum = baseRow + row;

            // Left side of aisle
            for (let pos = 0; pos < positionsPerRow; pos++) {
                const locX = currentX;
                const locY = this.y + (pos * dims.width);

                for (let lvl = 0; lvl < numLevels; lvl++) {
                    const level = levelFrom + lvl;
                    if (this.isLevelBlockedByTunnel('L', pos + 1, level)) {
                        continue;
                    }
                    const locId = this.formatLocationName(aisleNum, pos + 1, 'L', level);
                    this.locations.push({
                        location: locId,
                        x: locX,
                        y: locY,
                        zone: this.zone,
                        type: this.type,
                        level: level,
                        aisle: aisleNum,
                        position: pos + 1,
                        side: 'L'
                    });
                }
            }

            // Right side of aisle (back-to-back)
            for (let pos = 0; pos < positionsPerRow; pos++) {
                const locX = currentX + dims.depth + dims.aisleWidth;
                const locY = this.y + (pos * dims.width);

                for (let lvl = 0; lvl < numLevels; lvl++) {
                    const level = levelFrom + lvl;
                    if (this.isLevelBlockedByTunnel('R', pos + 1, level)) {
                        continue;
                    }
                    const locId = this.formatLocationName(aisleNum, pos + 1, 'R', level);
                    this.locations.push({
                        location: locId,
                        x: locX,
                        y: locY,
                        zone: this.zone,
                        type: this.type,
                        level: level,
                        aisle: aisleNum,
                        position: pos + 1,
                        side: 'R'
                    });
                }
            }

            currentX += rowWidth;
        }

        this.baseLocations = this.locations.map(loc => ({ ...loc }));
    }
}

window.AisleDrawer = AisleDrawer;
window.WarehouseDesignerAisle = Aisle;
