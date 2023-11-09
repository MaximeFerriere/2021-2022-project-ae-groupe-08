package be.vinci.pae.biz.object;

public interface Object extends ObjectDTO {

  boolean checkObjectStateEqualsPasDonne();

  boolean checkObjectStateCanBeReserved();

}
