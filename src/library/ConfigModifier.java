package library;

/**
 * An interface to change the GP as it is running
 * @author Roma
 *
 */
public interface ConfigModifier {
	
	/**
	 * Perform the change
	 * @param g the config
	 * @param pop the population
	 */
	public abstract void ModifyConfig(GPConfig g, Population pop); 
}
