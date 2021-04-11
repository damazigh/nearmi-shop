package org.nearmi.shop.repository.custom;

/**
 * provide business database access layer method for Product collection
 *
 * @author adjebarri
 * @since 0.1.0
 */
public interface ProductCustomRepository {

    /**
     * provide a method to verify if value field already exists
     *
     * @param value  actual value to check for unicity
     * @param field  collection field (where to check)
     * @param shopId shop product owner
     * @return true if the value doesn't already exist (search is limited to owner shop) false either
     */
    boolean isUnique(String value, String field, String shopId);
}
