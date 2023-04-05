/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.fileprocessing;

/**
 *
 * @author zmcmu
 */
import com.laserfiche.api.client.model.AccessKey;
import com.laserfiche.repository.api.RepositoryApiClient;
import com.laserfiche.repository.api.RepositoryApiClientImpl;
import com.laserfiche.repository.api.clients.impl.model.Entry;
import com.laserfiche.repository.api.clients.impl.model.ODataValueContextOfIListOfEntry;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class list {

    public ArrayList<Entrys> list(ArrayList<Entrys> entries, int max) {
        ArrayList<Entrys> result = new ArrayList<>();

        for (Entrys entry : entries) {
            if (entry.file.isDirectory()) {
                if (entry.location.equals("local")) {
                    File[] files = entry.file.listFiles();
                    if (files == null) {
                        continue;
                    }
                    int count = Math.min(max, files.length);
                    for (int i = 0; i < count; i++) {
                        result.add(new LocalEntry(files[i].getPath()));
                    }
                } else if (entry.location.equals("remte")) {
                    String servicePrincipalKey = "9BfKV0AUdVsctHoG8R-8";
                    String accessKeyBase64 = "ewoJImN1c3RvbWVySWQiOiAiMTQwMTM1OTIzOCIsCgkiY2xpZW50SWQiOiAiZWRjZjNjMTktMTE4Zi00ZTA4LTk3ZDEtODg5OTBiY2MxMzRjIiwKCSJkb21haW4iOiAibGFzZXJmaWNoZS5jYSIsCgkiandrIjogewoJCSJrdHkiOiAiRUMiLAoJCSJjcnYiOiAiUC0yNTYiLAoJCSJ1c2UiOiAic2lnIiwKCQkia2lkIjogImFha3oySUIySC12ZVlXVHY4czRxX0ZlREU5Mk5Edi12eTk1Z1N4MDVwbTQiLAoJCSJ4IjogInRNSGVQU2NCVVYzTUlacXlUUkN0c01tdUd0M2E3NWlJVVdfLXJ5LXN1dE0iLAoJCSJ5IjogIjBaSVd0MWMzdVJWdTRRSnJZMzhrRHlaMTNVYmdkSUc0cENkalQ1cGhPOEkiLAoJCSJkIjogIjVsN1ZEa3ZxUm5taXFEb3gwbWc3Z3FVUTl4Z296OXY2clNiLWJ1NDFsVGsiLAoJCSJpYXQiOiAxNjc3Mjk3NzczCgl9Cn0=";
                    String repositoryId = ((RemteEntry) entry).getRepoId();
                    
                    AccessKey accessKey = AccessKey.createFromBase64EncodedAccessKey(accessKeyBase64);

                    RepositoryApiClient client = RepositoryApiClientImpl.createFromAccessKey(
                            servicePrincipalKey, accessKey);
                    
                    ODataValueContextOfIListOfEntry files = client
                            .getEntriesClient()
                            .getEntryListing(repositoryId,((RemteEntry) entry).getEntryId() , true, null, null, null, null, null, "name", null, null, null).join();
                    List<Entry> filesV2 = files.getValue();
                    
                    int count = Math.min(max, filesV2.size());
                    for (int i = 0; i < count; i++) {
                        result.add(new RemteEntry(repositoryId,filesV2.get(i).getId()));
                    }
                    client.close();
                }
            } else {
                result.add(entry);
            }

        }

        return result;
    }

}
