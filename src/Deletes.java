package src;

import net.spy.memcached.internal.OperationFuture;

import com.couchbase.client.CouchbaseClient;

/*
 * Module that runs delete operations
 */

public class Deletes {
    public static void del_items (CouchbaseClient client, Variables V, String _prefix) {
        int items_to_delete = (int)(V.getDelRatio() * V.getItemCount());
        for (int i=0; i<items_to_delete; i++) {
            String key = String.format("%s%d", _prefix, i);
            OperationFuture<Boolean> delOp = null;
            try {
                delOp = client.delete(key);
            } catch (Exception e) {
                // TODO: Handle error that occurred during deleting?
                continue;
            }
            try {
                if (delOp.get().booleanValue()) {
                    // TODO: Something I'd guess, as deleted item still exists
                }
            } catch (Exception e) {
                // e.printStackTrace();
            }
        }
    }
}
