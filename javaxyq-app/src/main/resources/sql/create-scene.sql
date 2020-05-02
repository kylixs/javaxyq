drop table if exists scene;

create table scene (
		id bigint primary key,
		name varchar(30) not null,
		description varchar(200)
	);

create index scene_name_idx on scene (name);



