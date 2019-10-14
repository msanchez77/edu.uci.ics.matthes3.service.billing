DELIMITER $$

DROP PROCEDURE insert_sales_transactions$$
CREATE PROCEDURE insert_sales_transactions (
    IN s_email VARCHAR(50), IN s_movieId VARCHAR(10),
    IN s_quantity INT, IN s_saleDate DATE, IN t_token VARCHAR(50))
BEGIN
    INSERT INTO sales (email, movieId, quantity, saleDate)
        VALUES(s_email, s_movieId, s_quantity, s_saleDate);
    INSERT INTO transactions (sId, token)
        VALUES(LAST_INSERT_ID(), t_token);
END$$

DELIMITER ;

