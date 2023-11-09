package be.vinci.pae.biz.user;

public class InterestImpl implements InterestDTO {

  private UserDTO userDTO;
  private String timeSlots;
  private boolean conversationNeeded;

  @Override
  public UserDTO getUserDTO() {
    return userDTO;
  }

  @Override
  public void setUserDTO(UserDTO userDTO) {
    this.userDTO = userDTO;
  }

  @Override
  public String getTimeSlots() {
    return timeSlots;
  }

  @Override
  public void setTimeSlots(String timeSlots) {
    this.timeSlots = timeSlots;
  }

  @Override
  public boolean isConversationNeeded() {
    return conversationNeeded;
  }

  @Override
  public void setConversationNeeded(boolean conversationNeeded) {
    this.conversationNeeded = conversationNeeded;
  }
}
