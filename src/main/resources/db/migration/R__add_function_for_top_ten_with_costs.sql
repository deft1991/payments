create or replace function amount_receivers() RETURNS setof return_top_receiver_type AS $$

select totalAmount.receiver_id, sum(totalAmount.amount) as sum
from
     (SELECT bd2.id, bd2.receiver_id, bd2.amount
      FROM dblink('dbname=two_db port=5432 host=localhost user=root password=root',
         'select id, receiver_id, amount from payment.payment_data')
         AS bd2(id varchar(36), receiver_id varchar(36), amount numeric)
      union
         SELECT bd3.id, bd3.receiver_id, bd3.amount
         FROM dblink('dbname=three_db port=5432 host=localhost user=root password=root',
         'select id, receiver_id, amount from payment.payment_data')
         AS bd3(id varchar(36), receiver_id varchar(36), amount numeric)
      union
         select id, receiver_id, amount from payment.payment_data) as totalAmount
group by totalAmount.receiver_id
order by sum desc

    $$ LANGUAGE sql STABLE;
