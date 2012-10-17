
/* First created by JCasGen Sun Oct 14 20:33:41 EDT 2012 */
package model;

import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.cas.impl.CASImpl;
import org.apache.uima.cas.impl.FSGenerator;
import org.apache.uima.cas.FeatureStructure;
import org.apache.uima.cas.impl.TypeImpl;
import org.apache.uima.cas.Type;
import org.apache.uima.cas.impl.FeatureImpl;
import org.apache.uima.cas.Feature;
import org.apache.uima.jcas.tcas.Annotation_Type;

/** 
 * Updated by JCasGen Sun Oct 14 20:33:41 EDT 2012
 * @generated */
public class GeneName_Type extends Annotation_Type {
  /** @generated */
  @Override
  protected FSGenerator getFSGenerator() {return fsGenerator;}
  /** @generated */
  private final FSGenerator fsGenerator = 
    new FSGenerator() {
      public FeatureStructure createFS(int addr, CASImpl cas) {
  			 if (GeneName_Type.this.useExistingInstance) {
  			   // Return eq fs instance if already created
  		     FeatureStructure fs = GeneName_Type.this.jcas.getJfsFromCaddr(addr);
  		     if (null == fs) {
  		       fs = new GeneName(addr, GeneName_Type.this);
  			   GeneName_Type.this.jcas.putJfsFromCaddr(addr, fs);
  			   return fs;
  		     }
  		     return fs;
        } else return new GeneName(addr, GeneName_Type.this);
  	  }
    };
  /** @generated */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = GeneName.typeIndexID;
  /** @generated 
     @modifiable */
  @SuppressWarnings ("hiding")
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("model.GeneName");
 
  /** @generated */
  final Feature casFeat_ID;
  /** @generated */
  final int     casFeatCode_ID;
  /** @generated */ 
  public String getID(int addr) {
        if (featOkTst && casFeat_ID == null)
      jcas.throwFeatMissing("ID", "model.GeneName");
    return ll_cas.ll_getStringValue(addr, casFeatCode_ID);
  }
  /** @generated */    
  public void setID(int addr, String v) {
        if (featOkTst && casFeat_ID == null)
      jcas.throwFeatMissing("ID", "model.GeneName");
    ll_cas.ll_setStringValue(addr, casFeatCode_ID, v);}
    
  
 
  /** @generated */
  final Feature casFeat_Name;
  /** @generated */
  final int     casFeatCode_Name;
  /** @generated */ 
  public String getName(int addr) {
        if (featOkTst && casFeat_Name == null)
      jcas.throwFeatMissing("Name", "model.GeneName");
    return ll_cas.ll_getStringValue(addr, casFeatCode_Name);
  }
  /** @generated */    
  public void setName(int addr, String v) {
        if (featOkTst && casFeat_Name == null)
      jcas.throwFeatMissing("Name", "model.GeneName");
    ll_cas.ll_setStringValue(addr, casFeatCode_Name, v);}
    
  
 
  /** @generated */
  final Feature casFeat_Accumulate;
  /** @generated */
  final int     casFeatCode_Accumulate;
  /** @generated */ 
  public int getAccumulate(int addr) {
        if (featOkTst && casFeat_Accumulate == null)
      jcas.throwFeatMissing("Accumulate", "model.GeneName");
    return ll_cas.ll_getIntValue(addr, casFeatCode_Accumulate);
  }
  /** @generated */    
  public void setAccumulate(int addr, int v) {
        if (featOkTst && casFeat_Accumulate == null)
      jcas.throwFeatMissing("Accumulate", "model.GeneName");
    ll_cas.ll_setIntValue(addr, casFeatCode_Accumulate, v);}
    
  



  /** initialize variables to correspond with Cas Type and Features
	* @generated */
  public GeneName_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_ID = jcas.getRequiredFeatureDE(casType, "ID", "uima.cas.String", featOkTst);
    casFeatCode_ID  = (null == casFeat_ID) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_ID).getCode();

 
    casFeat_Name = jcas.getRequiredFeatureDE(casType, "Name", "uima.cas.String", featOkTst);
    casFeatCode_Name  = (null == casFeat_Name) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_Name).getCode();

 
    casFeat_Accumulate = jcas.getRequiredFeatureDE(casType, "Accumulate", "uima.cas.Integer", featOkTst);
    casFeatCode_Accumulate  = (null == casFeat_Accumulate) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_Accumulate).getCode();

  }
}



    