public class VoteSystem {
	public UserDBHandler userDB;
	public VoteDBHandler voteDB;
	public int ID;
	
	public VoteSystem()
	{
		userDB = new UserDBHandler();
		voteDB = new VoteDBHandler();
	}
	
	public static void main(String[] args) {
		VoteSystem voteSystem = new VoteSystem();
		
		voteSystem.userDB.createDB();
		voteSystem.userDB.createTable();
		voteSystem.userDB.insertUsers();
		
		voteSystem.voteDB.createTable();
		voteSystem.voteDB.insertCandidates();
		
		VotingWindow open = new VotingWindow(voteSystem);
		open.setVisible(true);
	}
	
	public int login(String username, String password, String role)
	{
		int status = -100;
		if ((ID = userDB.findUser(username, role)) > 0)
		{
			if (userDB.correctLogin(username, password, role))
			{
				if (userDB.getUserNumVotes(ID) == 0 && userDB.getLoginAttempts(ID) < 3)
				{
					//allow voting
					userDB.incrementLoginAttempts(ID);
					System.out.println("User can vote");
					status = 200;
				}
				else if (userDB.getUserNumVotes(ID) == 0 && userDB.getLoginAttempts(ID) >= 3)
				{
					//do not allow voting
					System.out.println("Too many login attempts");
					status = 50;
				}
				else if (userDB.getUserNumVotes(ID) != 0)
				{
					//do not allow voting
					System.out.println("User already voted once.");
					status = 250;
				}
			}
			else
			{
				//increment login attempts
				userDB.incrementLoginAttempts(ID);
				System.out.println("Incorrect login info");
				status = 150;
			}
		}
		else
		{
			System.out.println("Not a registered user");
			status = 100;
		}
		return status;
	}
	

	
	public void getResults()
	{
		
	}
	
	public String certifyVotes()
	{
		int numPeopleWhoVoted = userDB.getNumVotersWhoVoted();
		int numVotesSubmitted = voteDB.getTotalNumVotes();
		
		System.out.println("The number of people who voted: " + numPeopleWhoVoted);
		System.out.println("The sum of all votes in final count: " + numVotesSubmitted);
		
		String formattedOutput = "";
		
		return formattedOutput;
	}

}
