/**
 * 
 */
package massim.competition2015.scenario;

import java.io.Serializable;

/**
 * @author fschlesinger
 *
 */
public class PricedJob extends Job implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public int price;

	/**
	 * 
	 */
	public PricedJob() {
		super();
	}
	
	public PricedJob(String id){
		super(id);
	}
	
	public int getReward(){
		return price;
	}

}
