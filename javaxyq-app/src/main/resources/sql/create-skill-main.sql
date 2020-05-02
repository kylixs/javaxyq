drop table if exists skill_main;

create table skill_main (
		id bigint primary key,
        school varchar(10) not null,
		name varchar(20) not null,
		description varchar(400),
		effection varchar(50),
        magic_skill varchar(300)
        -- basic_skill varchar(10),
        -- level bigint not null
	);


