package jp.co.nittsu.gwh.module.auth.repository;

import jp.co.nittsu.gwh.module.auth.dto.TenantOption;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

/**
 * Repository to query VGWH_TM_CUST view for tenant options.
 * Returns warehouse + customer combinations for a given company code.
 */
@Repository
public class CustomerMasterRepository {

    private static final String[] CUSTOMER_SOURCE_CANDIDATES = {
            "VGWH_TM_CUST",
            "GWH.VGWH_TM_CUST",
            "SGWH0001.VGWH_TM_CUST",
            "GWH_TM_CUST",
            "GWH.GWH_TM_CUST",
            "SGWH0001.GWH_TM_CUST"
    };

    @PersistenceContext
    private EntityManager em;

    /**
     * Find all active customer-warehouse combinations for a company.
     */
    @SuppressWarnings("unchecked")
    public List<TenantOption> findTenantsByCompanyCode(String companyCode) {
        RuntimeException lastError = null;
        for (String source : CUSTOMER_SOURCE_CANDIDATES) {
            try {
                String sql = "SELECT CUST_WHS_COD, CUST_COD, CUST_NAM1 " +
                        "FROM " + source + " " +
                        "WHERE CUST_CPNY_COD = :cpnyCod " +
                        "AND DEL_FLG = 0 " +
                        "ORDER BY CUST_WHS_COD, CUST_COD";

                Query query = em.createNativeQuery(sql);
                query.setParameter("cpnyCod", companyCode);
                List<Object[]> rows = query.getResultList();

                List<TenantOption> tenants = new ArrayList<>();
                for (Object[] row : rows) {
                    String whsCod = row[0] != null ? row[0].toString() : "";
                    String custCod = row[1] != null ? row[1].toString() : "";
                    String custNam = row[2] != null ? row[2].toString() : "";
                    tenants.add(new TenantOption(whsCod, custCod, custNam));
                }
                return tenants;
            } catch (RuntimeException ex) {
                if (!isObjectNotFound(ex)) {
                    throw ex;
                }
                lastError = ex;
            }
        }

        if (lastError != null) {
            throw lastError;
        }
        return new ArrayList<>();
    }

    private boolean isObjectNotFound(Throwable throwable) {
        Throwable current = throwable;
        while (current != null) {
            String message = current.getMessage();
            if (message != null && message.contains("ORA-00942")) {
                return true;
            }
            current = current.getCause();
        }
        return false;
    }
}
