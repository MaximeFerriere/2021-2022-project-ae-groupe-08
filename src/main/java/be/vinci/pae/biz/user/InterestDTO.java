package be.vinci.pae.biz.user;

public interface InterestDTO {

  UserDTO getUserDTO();

  void setUserDTO(UserDTO userDTO);

  String getTimeSlots();

  void setTimeSlots(String timeSlots);

  boolean isConversationNeeded();

  void setConversationNeeded(boolean conversationNeeded);
}
