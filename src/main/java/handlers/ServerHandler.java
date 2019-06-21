package handlers;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.User;

public class ServerHandler{

	/**
	 * 
	 * @param o is the array of users. o cannot have a length greater than 2. Author is usually the first element
	 * @return 0 if they're equal, 1 if the first element is greater than the second element, otherwise -1
	 * @throws Exception if o contains more than two users
	 */
	public static int compareRoles(User[] o, Guild g) throws Exception {
		
		if(o.length > 2) {
			throw new Exception("Cannot compare more than two users!");
		} else if(o.length == 0) {
			throw new Exception("There is nothing to compare!");
		} else {
			
			int rolePos[] = new int[2]; // 1st Element is usually the author
			
			for(int i = 0; i < 2; i++) {
				
				for(Role r: g.getMember(o[i]).getRoles()) {
					
					if(r.getPosition() > rolePos[i])
						rolePos[i] = r.getPosition();
					
				}
				
			}
			
		}
		
		return 0;
	}
	
}
