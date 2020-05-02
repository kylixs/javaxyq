drop table if exists scene_teleporter;

create table scene_teleporter (
		id bigint primary key,
		start_id bigint not null,
		end_id bigint not null,
		start_point varchar(8) not null,
		end_point varchar(8) not null,
		description varchar(200)
	);

create index scene_teleporter_end_id_idx on scene_teleporter (end_id);

create index scene_teleporter_start_id_idx on scene_teleporter (start_id);



