drop table if exists item_medicine;

create table item_medicine (
		id bigint not null primary key,
		name varchar(20) not null,
		description varchar(400),
		price int default 0,
		hp int default 0,
		mp int default 0,
		injury int default 0,
		type varchar(10),
		efficacy varchar(400),
		level int default 0
	);