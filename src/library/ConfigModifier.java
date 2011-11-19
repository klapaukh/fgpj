package library;

public abstract class ConfigModifier {
	public GPConfig config;
	
	public ConfigModifier(GPConfig conf){
		this.config = conf;
	}
	
	public abstract void ModifyConfig(); 
}
