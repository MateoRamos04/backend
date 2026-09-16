package ar.edu.univ.repository;

import ar.edu.univ.model.CotizacionEntity;

public class CotizacionRepository extends GenericRepository<CotizacionEntity, String> {

    @Override
    protected Class<CotizacionEntity> getEntityClass() {
        return CotizacionEntity.class;
    }

}