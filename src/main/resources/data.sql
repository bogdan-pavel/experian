DROP table if exists credit_score;

create table credit_score
(
  msg_id            LONG ,
  company_name      VARCHAR(100) ,
  registration_date  varchar(100)  ,
  score             float ,
  directors_count   int  ,
  last_updated      varchar(100) 
);