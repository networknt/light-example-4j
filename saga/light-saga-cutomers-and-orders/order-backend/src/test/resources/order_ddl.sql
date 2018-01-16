
DROP table IF EXISTS  order_detail;



CREATE  TABLE order_detail (
  order_id long ,
  customer_id long,
  state varchar(50),
  amount numeric

)