package src;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.codehaus.jettison.json.JSONException;

import net.spy.memcached.internal.OperationFuture;

import com.couchbase.client.CouchbaseClient;

/*
 * Module that runs set operations, and sets with expires
 */

public class Sets {
    public static void set_items (CouchbaseClient client, Variables V, String _prefix) throws JSONException {
        Random gen = new Random(987654321);
        int items_to_expire = (int)(V.getExpRatio() * V.getItemCount());
        List<OperationFuture<Boolean>> sets = new LinkedList<OperationFuture<Boolean>>();
        for (int i=0; i<(V.getItemCount() - items_to_expire); i++) {
            OperationFuture<Boolean> setOp;
            String key = String.format("%s%d", _prefix, i);
            if (V.isJson()) {
                setOp = client.set(key, Gen.retrieveJSON(gen, V).toString());
            } else {
                setOp = client.set(key, Gen.retrieveBinary(V));
            }
            sets.add(setOp);
        }

        for (int i=(V.getItemCount() - items_to_expire); i<V.getItemCount(); i++) {
            OperationFuture<Boolean> setOp;
            String key = String.format("%s%d", _prefix, i);
            if (V.isJson()) {
                setOp = client.set(key, V.getExpiration(), Gen.retrieveJSON(gen, V).toString());
            } else {
                setOp = client.set(key, V.getExpiration(), Gen.retrieveBinary(V));
            }
            sets.add(setOp);
        }

        while (!sets.isEmpty()) {
            try {
                if (sets.get(0).get().booleanValue() == false) {
                    // TODO: Something I'd guess, as set failed
                }
            } catch (Exception e) {
                // e.printStackTrace();
            }
            sets.remove(0);
        }
    }
}
