/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.mycompany.fileprocessing;

import com.nimbusds.jose.shaded.json.JSONArray;
import com.nimbusds.jose.shaded.json.JSONObject;
import com.nimbusds.jose.shaded.json.parser.JSONParser;
import java.io.FileReader;
import java.util.ArrayList;

/**
 *
 * @author zmcmu
 */
public class FileProcessing {

    public static void main(String[] args) {
        ArrayList<Entrys> inputList = new ArrayList<>();
        String key = null;
        String operator = null;
        long length = 0;
        int min = 0;
        int lines = 0;
        int max = 0;
        String suffix = null;

        JSONParser jsonParser = new JSONParser();
        try (FileReader reader = new FileReader("C:\\Users\\zmcmu\\OneDrive\\Documents\\NetBeansProjects\\FileProcessing\\jasonJsonCount.txt")) {
            Object obj = jsonParser.parse(reader);//make json object

            JSONObject j = (JSONObject) obj;
            JSONArray processing_elements = (JSONArray) j.get("processing_elements");
            JSONObject firstElement = (JSONObject) processing_elements.get(0);
            JSONArray input_entries = (JSONArray) firstElement.get("input_entries");//get the list of input files

            for (int x = 0; x < input_entries.size(); x++) {
                JSONObject temp = (JSONObject) input_entries.get(x);

                if (temp.get("type").equals("local")) {//add local entrys to the array
                    try {
                        String path = (String) temp.get("path");
                        inputList.add(new LocalEntry(path));
                    } catch (Exception d) {
                        d.printStackTrace();
                    }
                } else if (temp.get("type").equals("remte")) {//add remte entrys to the array 
                    String repoId = (String) temp.get("repositoryId");
                    long entNum = (long) temp.getAsNumber("entryId");
                    inputList.add(new RemteEntry(repoId, (int) entNum));
                }
            }

            JSONArray parameters;
            for (int x = 0; x < processing_elements.size(); x++) {//repeat for each processing element
                JSONObject tempElement = (JSONObject) processing_elements.get(x);
                String processingType = tempElement.getAsString("type");//get what kind of element
                switch (processingType) {
                    case "Name Filter"://if the json says Name Filter
                        System.out.println("Name filter");
                        parameters = (JSONArray) tempElement.get("parameters");//get array of parameters
                        for (int y = 0; y < parameters.size(); y++) {//repeat for each parameter
                            JSONObject pN = (JSONObject) parameters.get(y);
                            String pName = (String) pN.getAsString("name");
                            switch (pName) {//asign to useable varuble 
                                case "Key":
                                    key = (String) pN.getAsString("value");
                                    break;
                                default:
                                    System.out.println("name in json file does not line up with parameters");
                            }
                        }
                        Name nFilter = new Name();//make name object
                        inputList = new ArrayList(nFilter.nameFilter(inputList, key));//apply filter 
                        break;

                    case "Length Filter":
                        System.out.println("Length Filter");

                        parameters = (JSONArray) tempElement.get("parameters");
                        for (int y = 0; y < parameters.size(); y++) {
                            JSONObject pN = (JSONObject) parameters.get(y);
                            String pName = (String) pN.getAsString("name");
                            switch (pName) {
                                case "Operator":
                                    operator = pN.getAsString("value");
                                    break;
                                case "Length":
                                    length = (long) pN.getAsNumber("value");
                                    break;
                                default:
                                    System.out.println("name in json file does not line up with parameters");
                            }
                        }
                        Length lFilter = new Length();
                        inputList = new ArrayList(lFilter.lenghtFilter(inputList, operator, length));
                        break;

                    case "Content Filter":
                        System.out.println("Content Filter");
                        parameters = (JSONArray) tempElement.get("parameters");
                        for (int y = 0; y < parameters.size(); y++) {
                            JSONObject pN = (JSONObject) parameters.get(y);
                            String pName = (String) pN.getAsString("name");
                            switch (pName) {
                                case "Key":
                                    key = (String) pN.getAsString("value");
                                    break;
                                default:
                                    System.out.println("name in json file does not line up with parameters");
                            }
                        }
                        ContentFilter cFFilter = new ContentFilter();
                        inputList = new ArrayList(cFFilter.filter(inputList, key));

                        break;
                    case "Count Filter":
                        System.out.println("Count Filter");
                        parameters = (JSONArray) tempElement.get("parameters");
                        for (int y = 0; y < parameters.size(); y++) {
                            JSONObject pN = (JSONObject) parameters.get(y);
                            String pName = (String) pN.getAsString("name");
                            switch (pName) {
                                case "Key":
                                    key = pN.getAsString("value");
                                    break;
                                case "Min":
                                    min = pN.getAsNumber("value").intValue();
                                    break;
                                default:
                                    System.out.println("name in json file does not line up with parameters");
                            }
                        }

                        Count cFilter = new Count();
                        inputList = new ArrayList(cFilter.filter(inputList, key, min));
                        break;
                    case "Split":
                        System.out.println("Split");
                        parameters = (JSONArray) tempElement.get("parameters");
                        for (int y = 0; y < parameters.size(); y++) {
                            JSONObject pN = (JSONObject) parameters.get(y);
                            String pName = (String) pN.getAsString("name");
                            switch (pName) {
                                case "Lines":
                                    lines = pN.getAsNumber("value").intValue();
                                    break;
                                default:
                                    System.out.println("name in json file does not line up with parameters");
                            }
                        }

                        Split fileSplit = new Split(inputList, lines);
                        inputList = fileSplit.split();

                        break;
                    case "List":
                        System.out.println("List");
                        parameters = (JSONArray) tempElement.get("parameters");
                        for (int y = 0; y < parameters.size(); y++) {
                            JSONObject pN = (JSONObject) parameters.get(y);
                            String pName = (String) pN.getAsString("name");
                            switch (pName) {
                                case "Max":
                                    max = pN.getAsNumber("value").intValue();
                                    break;
                                default:
                                    System.out.println("name in json file does not line up with parameters");
                            }

                        }

                        list list = new list();
                        inputList = list.list(inputList, max);
                        break;
                    case "Rename":
                        System.out.println("Rename");
                        parameters = (JSONArray) tempElement.get("parameters");
                        for (int y = 0; y < parameters.size(); y++) {
                            JSONObject pN = (JSONObject) parameters.get(y);
                            String pName = (String) pN.getAsString("name");
                            switch (pName) {
                                case "Suffix":
                                    suffix = pN.getAsString("value");
                                    break;
                                default:
                                    System.out.println("name in json file does not line up with parameters");
                            }

                            Rename rename = new Rename();
                            rename.renamePE(inputList, suffix);
                        }
                        break;
                    case "Print":
                        System.out.println("Print");

                        PrintProcessingElement print = new PrintProcessingElement();
                        print.printEntries(inputList);
                        break;
                    default:
                        break;
                }

            }
            reader.close();
        } catch (Exception a) {
            a.printStackTrace();
        }
    }
}
