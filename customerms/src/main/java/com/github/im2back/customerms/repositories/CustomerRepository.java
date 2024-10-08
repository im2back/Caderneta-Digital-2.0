package com.github.im2back.customerms.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.github.im2back.customerms.model.entities.customer.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

	Optional<Customer> findByDocument(String document);

	Optional<Customer> findByEmail(String email);

	Optional<Customer> findByPhone(String phone);

	@Query("SELECT c FROM Customer c JOIN c.purchaseRecord p WHERE p.id = :purchaseId")
	Optional<Customer> findByPurchase(@Param("purchaseId") Long purchaseId);

	@Query("SELECT SUM(pr.productprice * pr.quantity) " + "FROM Customer c JOIN c.purchaseRecord pr "
			+ "WHERE (YEAR(pr.purchaseDate) = YEAR(CURRENT_DATE) AND MONTH(pr.purchaseDate) = MONTH(CURRENT_DATE) - 1) OR "
			+ "(YEAR(pr.purchaseDate) = YEAR(CURRENT_DATE) - 1 AND MONTH(pr.purchaseDate) = 12 AND MONTH(CURRENT_DATE) = 1)")
	Double totalValueForLastMonth();

	@Query("SELECT SUM(pr.productprice * pr.quantity) " + "FROM Customer c JOIN c.purchaseRecord pr"
			+ " WHERE pr.purchaseDate >= FUNCTION('DATE_FORMAT', CURRENT_DATE, '%Y-%m-01')")
	Double partialValueOfTheCurrentMonth();

	@Query("SELECT SUM(pr.productprice * pr.quantity) " + "FROM Customer c JOIN c.purchaseRecord pr "
			+ " WHERE DATE(pr.purchaseDate) = CURRENT_DATE")
	Double partialVAlueForCurrentDay();
	
	@Query(value = "SELECT DATE(purchase_date) AS purchaseDate, SUM(product_price * product_quantity) AS totalValue " +
            "FROM tb_purchase " +
            "WHERE DATE(purchase_date) BETWEEN CURDATE() - INTERVAL 7 DAY AND CURDATE() - INTERVAL 1 DAY " +
            "GROUP BY DATE(purchase_date) " +
            "ORDER BY purchaseDate DESC", nativeQuery = true)
	List<Object[]> findTotalValueForLast7DaysExcludingToday();

	 
	@Query("SELECT SUM(pr.productprice * pr.quantity) FROM PurchaseRecord pr WHERE pr.status = EM_ABERTO")
	Double totalOutstandingAmount();

}
