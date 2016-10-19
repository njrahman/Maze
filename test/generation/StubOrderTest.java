package generation;

public class StubOrderTest implements Order{
	private int skill;
	private Builder builder;
	private boolean perfect;
	private MazeConfiguration mazeConfiguration;
	int percentDone;
	
	public StubOrderTest(int skill, Builder builder, boolean perfect){
		this.skill = skill;
		this.builder = builder;
		this.perfect= perfect;
	}
	
	@Override
	public int getSkillLevel() {
		return skill;
	}
	
	@Override
	public Builder getBuilder() {
		return builder;
	}
	
	@Override
	public boolean isPerfect() {
		return perfect;
	}
	
	@Override
	public void deliver(MazeConfiguration mazeConfig) {
		this.mazeConfiguration = mazeConfig;
	}
	
	@Override
	public void updateProgress(int percentage) {
		this.percentDone = percentage;
	}
	public MazeConfiguration getConfiguration(){
		return mazeConfiguration;
	}
}