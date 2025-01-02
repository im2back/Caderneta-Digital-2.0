package com.github.im2back.customerms.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.github.im2back.customerms.model.entities.customer.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
	
	@Query("SELECT c FROM Customer c WHERE c.email = :email OR c.document = :document OR c.phone = :phone")
	List<Customer> findByEmailOrDocumentOrPhone(@Param("email") String email, 
	                                            @Param("document") String document, 
	                                            @Param("phone") String phone);

	@Query("SELECT c FROM Customer c LEFT JOIN FETCH c.purchaseRecord WHERE c.document = :document")
	Optional<Customer> findByDocument(@Param("document") String document);

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

	
	@Modifying
	@Query(value = "UPDATE tb_purchase p " +
	               "SET p.payment_status = :newStatus " +
	               "WHERE p.customer_id = (SELECT c.id FROM tb_customer c WHERE c.document = :document) " +
	               "AND p.payment_status = :currentStatus", 
	       nativeQuery = true)
	void updateStatusByCustomerDocumentNative(@Param("newStatus") String newStatus, 
	                                          @Param("document") String document, 
	                                          @Param("currentStatus") String currentStatus);
	
	@Modifying
	@Query(value = "DELETE FROM tb_purchase WHERE id = :idPurchase", nativeQuery = true)
	void undoPurchase(@Param("idPurchase") Long idPurchase);
	
	@Modifying
	@Query(value = "UPDATE tb_purchase p SET p.payment_status = :paid WHERE p.id = :idPurchase ", nativeQuery = true)
	void individualPayment(@Param("paid")String status, @Param("idPurchase") Long idPurchase);


		
}
