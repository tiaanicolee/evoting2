public class VoteSystem {
	public UserDBHandler userDB;
	public VoteDBHandler voteDB;
	public Integer ID;
	
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
	
	public int login(String username, Integer id, String role)
	{
		int status = -100;
		if (userDB.findUser(username, role) > 0)
		{
			if ((ID = userDB.correctLogin(username, id, role)) > 0)
			{
				if (userDB.getUserNumVotes(id) == 0 && userDB.getLoginAttempts(id) < 3)
				{
					//allow voting
					System.out.println("User can vote");
					status = 200;
				}
				else if (userDB.getUserNumVotes(id) == 0 && userDB.getLoginAttempts(id) >= 3)
				{
					//do not allow voting
					System.out.println("Too many login attempts");
					status = 50;
				}
				else if (userDB.getUserNumVotes(id) != 0)
				{
					//do not allow voting
					System.out.println("User already voted once.");
					status = 250;
				}
			}
			else
			{
				//increment login attempts
				userDB.incrementLoginAttempts(id);
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
	

	
	public void submit(String candName)
	{
		userDB.setVoteCount(ID);
		voteDB.printRecount(candName, ID);
		voteDB.saveVote(candName);
	}
	
	public String certifyVotes()
	{
		int numPeopleWhoVoted = userDB.getNumVotersWhoVoted();
		int numVotesSubmitted = voteDB.getTotalNumVotes();
		
		System.out.println("The number of people who voted: " + numPeopleWhoVoted);
		System.out.println(" The sum of all votes in final count: " + numVotesSubmitted);
		String formattedOutput = "The number of people who voted: " + numPeopleWhoVoted + 
				" The sum of all votes in final count: " + numVotesSubmitted;
		if (numPeopleWhoVoted != numVotesSubmitted)
			formattedOutput =  "The number of people who voted: " + numPeopleWhoVoted + 
					" The sum of all votes in final count: " + numVotesSubmitted +
					" There is a discrepancy in the votes. Please examine recount.txt for manual count and repeated IDs";
		
		
		
		return formattedOutput;
	}

}
