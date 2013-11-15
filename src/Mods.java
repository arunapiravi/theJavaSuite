package src;

import java.util.LinkedList;
import java.util.List;

import net.spy.memcached.internal.OperationFuture;

import com.couchbase.client.CouchbaseClient;

/*
 * Module that runs append and prepend operations
 */

public class Mods {
    public static void mod_items (CouchbaseClient client, Variables V, String _prefix) {
        int point_of_reference = (int)(V.getDelRatio() * V.getItemCount());
        int items_to_append = (int)(V.getAppendRatio() * V.getItemCount());
        int items_to_prepend = (int)(V.getPrependRatio() * V.getItemCount());

        List<OperationFuture<Boolean>> mods = new LinkedList<OperationFuture<Boolean>>();
        for (int i=point_of_reference; i<items_to_append; i++) {
            OperationFuture<Boolean> appendOp;
            String key = String.format("%s%d", _prefix, i);
            appendOp = client.append(key, Gen.retrieveTandem(V, "append"));
            if (V.isCheckEnabled()) {
                mods.add(appendOp);
            }
        }

        for (int i=point_of_reference; i<items_to_prepend; i++) {
            OperationFuture<Boolean> prependOp;
            String key = String.format("%s%d", _prefix, i);
            prependOp = client.prepend(key, Gen.retrieveTandem(V, "prepend"));
            if (V.isCheckEnabled()) {
                mods.add(prependOp);
            }
        }

        while (!mods.isEmpty()) {
            try {
                if (mods.get(0).get().booleanValue() == false) {
                    // TODO: Something I'd guess, as mod failed
                }
            } catch (Exception e) {
                // e.printStackTrace();
            }
            mods.remove(0);
        }
    }
}
