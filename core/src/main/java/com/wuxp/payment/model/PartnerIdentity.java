package com.wuxp.payment.model;

import java.io.Serializable;

/**
 * 用于标记应用内的商家唯一标识
 *
 * @author wxup
 */
public interface PartnerIdentity extends Serializable {


    /**
     * 获取商户在应用内的标识
     *
     * @param <ID>
     * @return
     */
    default <ID> ID getPartnerId() {
        return null;
    }

}
