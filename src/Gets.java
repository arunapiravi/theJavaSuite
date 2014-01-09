package src;

import com.couchbase.client.CouchbaseClient;

/*
 * Module that runs get operations
 */

public class Gets {
    public static void get_items (CouchbaseClient client, Variables V, String _prefix) {
        boolean checkFlag = true;
        int count;
        if (V.getSetRatio() == 0.0) {
            count = V.getItemCount();
        } else {
            count = (int)(((1.0 - V.getSetRatio()) * V.getItemCount()) / V.getSetRatio());
        }
        while (checkFlag) {
            for (int i=0; i<V.getItemCount(); i++) {
                if (count == 0) {
                    checkFlag = false;
                    break;
                }
                Object item = client.get(String.format("%s%d", _prefix, i));
                if (item != null)
                    count --;
            }
        }
    }
}
