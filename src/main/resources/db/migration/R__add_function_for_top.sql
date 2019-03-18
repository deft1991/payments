create or replace function total_amount_by_sender_id(sender_id varchar) returns decimal as $$

select sum(totalAmount.amount)
from
     (SELECT bd2.amount
      FROM dblink('dbname=two_db port=5432 host=localhost user=root password=root',
         'select sum(amount) from payment.payment_data where sender_id= '||quote_literal ($1)||'')
         AS bd2(amount numeric)
      union
         SELECT bd3.amount
         FROM dblink('dbname=three_db port=5432 host=localhost user=root password=root',
         'select sum(amount) from payment.payment_data where sender_id= '||quote_literal ($1)||'')
         AS bd3(amount numeric)
      union
         select sum(amount) from payment.payment_data where sender_id= $1) as totalAmount;

$$ language 'sql';