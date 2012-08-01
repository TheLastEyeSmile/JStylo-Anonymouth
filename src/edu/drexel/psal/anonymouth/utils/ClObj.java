package edu.drexel.psal.anonymouth.utils;
import com.jgaap.generics.Document;

import edu.drexel.psal.anonymouth.gooie.BackendInterface;
import edu.drexel.psal.anonymouth.gooie.GUIMain;
import edu.drexel.psal.anonymouth.gooie.ThePresident;
import edu.drexel.psal.anonymouth.projectDev.DataAnalyzer;
import edu.drexel.psal.anonymouth.projectDev.DocumentMagician;
import edu.drexel.psal.anonymouth.utils.ConsolidationStation;
import edu.drexel.psal.anonymouth.utils.TaggedDocument;
import edu.drexel.psal.anonymouth.utils.Tagger;
import edu.drexel.psal.jstylo.generics.CumulativeFeatureDriver;
import edu.drexel.psal.jstylo.generics.Logger;
import edu.drexel.psal.jstylo.generics.ProblemSet;

import weka.classifiers.Classifier;
import weka.classifiers.functions.SMO;
import weka.clusterers.Clusterer;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Joe Muoio
 * Date: 7/13/12
 * Time: 10:44 AM
 * To change this template use File | Settings | File Templates.
 */


public class ClObj {


    private DocumentMagician magician;
    private DataAnalyzer wizard;
    private Classifier classifier;

   // private Clusterer clusterer;
    private String user;

    public static final int[] CLUSTERGROUP={6, 3, 5, 5, 5, 4, 3, 6, 5, 4, 2, 5, 4, 1, 5, 5, 4, 5, 5, 1, 1, 5, 3, 6, 1, 3, 2, 5, 5, 2, 4, 4, 3, 6, 3, 2, 2, 3, 2, 5, 2, 5, 5, 3, 2, 3, 4, 5, 3, 5, 6, 1, 6, 6, 4, 3, 1, 6, 4, 4, 5, 5, 2, 3, 6, 4, 5, 5, 1, 5, 6, 6, 4, 1, 2, 2, 3, 4, 2, 1, 5, 3, 2, 4, 6, 2, 6, 6, 1, 4, 4, 3, 5, 2, 4, 5, 5, 4, 2, 5, 1, 3, 1, 1, 1, 1, 3, 3, 4, 1, 4, 2, 1, 4, 2, 5, 4, 1, 1, 3, 1, 1, 2, 3, 4, 2, 4, 2, 4, 5, 1, 1, 1, 2, 3, 3, 3, 3, 3, 1, 3, 4, 2, 5, 2, 4, 2, 2, 4, 1, 3, 5, 1, 3, 2, 6, 3, 1, 1, 1, 5, 3, 3, 1, 1, 4, 6, 5, 2, 1, 2, 1, 5, 2, 4, 2, 2, 4, 5, 2, 3, 2, 3, 3, 2, 2, 1, 3, 3, 2, 5, 1, 2, 4, 1, 1, 4, 4, 3, 3};//put the agreed cluster here
    /**
     *
     * @param user The userName of the call.
     */
    public void initiate(String user,String doc)  {
        ProblemSet ps=makeProbSet(user);

        if (ps==null){
            System.out.println("THIS IS NULL OH NO");
        }
        
        wizard=new DataAnalyzer(ps,user);
        magician=new DocumentMagician(false);

        wizard.setNumFeaturesToReturn(200);
        
//        BackendInterface.preTargetSelectionProcessing(null,wizard,magician,null);
        magician.setModifiedDocument(doc);
        classifier=new SMO();
        CumulativeFeatureDriver cfd=makeCFD();
       /* try{
        	magician.initialDocToData(ps,cfd,classifier);
        }
        catch(Exception e){
            e.printStackTrace();
        }*/

//       TaggedDocument taggedDocument = new TaggedDocument();//eits.editorBox.getText();
//        ConsolidationStation.toModifyTaggedDocs=new ArrayList<TaggedDocument>();
//        ConsolidationStation.toModifyTaggedDocs.add(taggedDocument);

        try {
            wizard.runInitial(magician,cfd,classifier);//makes clusters?
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        magician.runWeka(user);
        wizard.selectedTargets=CLUSTERGROUP;
        wizard.setSelectedTargets();
    }
    /**
     *
     * @param user the name of the user.
     * @return a problem set consisting of the user's edited doc and the core corpus.
     */
    private ProblemSet makeProbSet(String user) {
        String path ="C:\\Users\\Joe Muoio\\Documents\\CM1Test.xml";
        //the path to the XML file holding the author corpus and other authors. Not docToAnonymize
        try{
        	ProblemSet.setDummyAuthor("");
            ProblemSet ps=new ProblemSet(path);
            ps.setTrainCorpusName("Authors");

            ps.setDummyAuthor("*"+user+"*");
            for(Document d:ps.getAllTrainDocs()){
            	d.load();
            }
            for(Document d:ps.getTestDocs()){
            	d.load();
        	}	
//            ArrayList<String> allTestDocPaths = new ArrayList<String>();
//            for (Document doc:ps.getTestDocs())
//                allTestDocPaths.add(doc.getFilePath());
//            path = file.getAbsolutePath();
            ps.addTestDoc(new Document(path,ProblemSet.getDummyAuthor(),user+"Combined.txt"));
//set for training docs from author
//            ps.addTestDoc(new Document(path,"_you_",user+"Combined.txt"));
           // System.out.println("PS: " + ps.toString());
//            Logger.logln("Document: "+ps.getAllTrainDocs().get(0).stringify());
            return ps;
        }
        catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     *
     * @return a cumulative feature driver using writeprints(limited)
     */
    private CumulativeFeatureDriver makeCFD(){
        String path="C:\\Users\\Joe Muoio\\Documents\\writeprints_feature_set_limited.xml";
        //the path to the XML that specifies the writeprints limited featureset.
//        Check this^^
        try{
            CumulativeFeatureDriver cfd=new CumulativeFeatureDriver(path);
            return cfd;
        }
        catch(Exception e){
            e.printStackTrace();
            return null;
        }

    }

    /**
     *
     * @param doc The docToAnonymize to update it.
     * @return A JSON object: {you:#,next:#}
     */
    public String classify(String doc) {
        //returns the classification as a json object {you:#,next:#}

        String toReturn="{\"you\":\"";
        try{
            //wizard.runInitial(magician, main.cfd, main.classifiers.get(0));
        	wizard.reRunModified(magician);
           // Tagger.initTagger();
           
            magician.runWeka(user);
        }
        catch(Exception e){
            e.printStackTrace();
        }

        Map<String,Map<String,Double>> wekaResults = magician.getWekaResultList();

        magician.setModifiedDocument(doc);

        ThePresident.sessionName=user;
        magician.reRunModified();


        String results=magician.getWekaResultList().toString();
      //Ex:  {c_01.txt={g=8.505846639079016E-5, s=0.09668999140090943, p=0.32670991438030655, m=1.0278853757273552E-8, ~* you *~=0.573871175168698, k=1.859359901351936E-4, h=0.0024579143147061733}}
        int start=results.indexOf("~* you *~")+10;//check this
        toReturn+=results.subSequence(start,results.indexOf(",",start))+"\"}";

       // toReturn+=",\"next\":\"";

        return toReturn;

    }

    public static void main(String[] args) throws Exception {
        final String user="jgm";
        final String doc="{ \"sentences\": \"This is a sentence from the server. This is a sentence from the\n" +
                "server. This is a sentence from the server. This is a sentence from the server.\n" +
                "This is a sentence from the server. This is a sentence from the server. This is\n" +
                "a sentence from the server. This is a sentence from the server. This is a senten\n" +
                "ce from the server. This is a sentence from the server. This is a sentence from\n" +
                "the server. This is a sentence from the server. \"}";
        String toReturn="";
        ClObj classTemp=new ClObj();
        classTemp.initiate(user,doc);
        toReturn=classTemp.classify(doc);
        System.out.println(toReturn);
    }

}