package com.javaxyq.config;

/**
 * @author 龚德伟
 * @history 2008-5-21 龚德伟 新建
 */
public class MapConfig implements Config {
	private String id;

	private String name;

	private String path;

	private String music;

	public MapConfig(String id, String name, String path, String music) {
		this.id = id;
		this.name = name;
		this.path = path;
		this.music = music;
	}

	public String getName() {
		return name;
	}

	public String getPath() {
		return path;
	}

	public String getType() {
		return "map";
	}

	public String getId() {
		return id;
	}

	@Override
	public String toString() {
		return "MapConfig{" + id + "," + name + "," + path + "}";
	}

	public String getMusic() {
		return music;
	}
}
