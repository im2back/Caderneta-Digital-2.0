package com.github.im2back.customerms.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.github.im2back.customerms.model.dto.dataoutput.SevenDayGraphData;
import com.github.im2back.customerms.model.entities.customer.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

	Optional<Customer> findByDocument(String document);

	Optional<Customer> findByEmail(String email);
	
	Optional<Customer> findByPhone(String phone);
	
    @Query("SELECT c FROM Customer c JOIN c.purchaseRecord p WHERE p.id = :purchaseId")
    Optional<Customer> findByPurchase(@Param("purchaseId") Long purchaseId);
    
    @Query("SELECT SUM(pr.productprice * pr.quantity) " +
            "FROM Customer c JOIN c.purchaseRecord pr " +
            "WHERE (YEAR(pr.purchaseDate) = YEAR(CURRENT_DATE) AND MONTH(pr.purchaseDate) = MONTH(CURRENT_DATE) - 1) OR " +
            "(YEAR(pr.purchaseDate) = YEAR(CURRENT_DATE) - 1 AND MONTH(pr.purchaseDate) = 12 AND MONTH(CURRENT_DATE) = 1)")
    Double totalValueForLastMonth();
    
	/*Consulta para MYSQL
    @Query("SELECT SUM(pr.productprice * pr.quantity) "
	  		+ "FROM Customer c JOIN c.purchaseRecord pr"
	  		+ " WHERE pr.purchaseDate >= FUNCTION('DATE_FORMAT', CURRENT_DATE, '%Y-%m-01')")
	Double amountStartofTheMonthUntilToday();*/
    
    //Consulta para h2
    @Query("SELECT SUM(pr.productprice * pr.quantity) " +
    	       "FROM Customer c JOIN c.purchaseRecord pr " +
    	       "WHERE pr.purchaseDate >= DATE_TRUNC('MONTH', CURRENT_DATE)")
    Double amountStartofTheMonthUntilTodayH2();
    
  /*Consulta para MYSQL
    @Query("SELECT SUM(pr.productprice * pr.quantity) " +
    	   "FROM Customer c JOIN c.purchaseRecord pr " +
    	   " WHERE DATE(pr.purchaseDate) = CURRENT_DATE")
	  Double valorTotalDoDia();
	  */
    
  //Consulta para h2
    @Query("SELECT SUM(pr.productprice * pr.quantity) " +
    	       "FROM Customer c JOIN c.purchaseRecord pr " +
    	       "WHERE CAST(pr.purchaseDate AS DATE) = CAST(CURRENT_DATE AS DATE)")
    	Double totalVAlueForTheDay();


    
    
    @Query("SELECT NEW com.github.im2back.customerms.model.dto.dataoutput.SevenDayGraphData(FUNCTION('DATE', pr.purchaseDate), SUM(pr.productprice * pr.quantity)) " +
    		"FROM Customer c JOIN c.purchaseRecord pr " +
            "WHERE FUNCTION('DATE', pr.purchaseDate) BETWEEN (CURRENT_DATE - 7) AND CURRENT_DATE " +
            "GROUP BY FUNCTION('DATE', pr.purchaseDate)" +
    		"ORDER BY FUNCTION('DATE', pr.purchaseDate) DESC")
  List<SevenDayGraphData> obterSomaPrecoPorDataUltimos7Dias();
  
    		
}
