drop table if exists item_weapon;

create table item_weapon (
		id bigint primary key,
		res_no varchar(20) ,
		level int default 0,
		name varchar(20),
		description varchar(400),
        [character] varchar(60),
		price int default 0,
		add_attribute1 int,
        add_attribute2 int,
        accuracy int,
		damage int,
        dodge int,
		type varchar(10),
        add_skill varchar(50),
		efficacy varchar(20)
	);

create index item_weapon_idx on item_weapon (type);

create unique index item_weapon_name on item_weapon (name);



