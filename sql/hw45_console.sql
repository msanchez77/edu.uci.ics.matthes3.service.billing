# SELECT customers.email, group_concat(movieId), group_concat(quantity)
# FROM customers LEFT OUTER JOIN carts ON customers.email = carts.email
# WHERE customers.email= "peteranteater@uci.edu"
# GROUP BY customers.email


# SELECT SUM(unit_price*discount)
# FROM movie_prices
# WHERE movieId IN ("tt0195119", "tt0196927", "tt0198748", "tt0197862")


# DROP PROCEDURE  insert_sales_transactions;
CREATE PROCEDURE insert_sales_transactions (
    IN s_email VARCHAR(50), IN s_movieId VARCHAR(10),
    IN s_quantity INT, IN s_saleDate DATE, IN t_token VARCHAR(50))
BEGIN
    INSERT INTO sales (email, movieId, quantity, saleDate)
        VALUES(s_email, s_movieId, s_quantity, s_saleDate);
    INSERT INTO transactions (sId, token)
        VALUES(LAST_INSERT_ID(), t_token);
END;

#
# # CALL insert_sales_transactions(
# #     'peteranteater@uci.edu', 'tt0195119',
# #     4, '2019-05-09', 'EC-8CR66102BU3608113')


# SELECT transactionId, s.email, group_concat(s.movieId), group_concat(s.quantity), group_concat(mp.unit_price), group_concat(mp.discount), group_concat(s.saleDate)
# FROM sales s
#     LEFT OUTER JOIN transactions t on s.id = t.sId
#     LEFT OUTER JOIN movie_prices mp on s.movieId = mp.movieId
# WHERE email = ?
# GROUP BY transactionId