drop table if exists skill_magic;

create table skill_magic (
		id bigint primary key,
        school varchar(10) not null,
		name varchar(20) not null,
		description varchar(400),
        effection varchar(150),
		conditions varchar(150),
        consumption varchar(200),
        type bigint,
        magic varchar(50),
        addon bigint,
        waddon bigint,
		action varchar(50)
	);


