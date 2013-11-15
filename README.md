theJavaSuite
============

Distribution of write-operations, over the key set:

    x - x - x - x - x - x - x - x - x sets x - x - x - x - x - x - x - x - x - x - x - x
    x - x deletes x - x
                       x - x - x appends x - x - x
                       x - x prepends x - x
                                                                     x - x expires x - x
                                                                                        x - x adds x - x


i.e, Say if the item-count was set at 1000 items and set ratio was like 0.4, sets-gets happen at ratio
2:3, until 1000 items are created and 1500 successful get requests are issued, the last (exp-ratio) number
of set items are set with an expiration time. After this, the first (del-ratio) number of items get deleted,
and the next (append-ratio) number of items get updated with the appends, and the overlapping (prepend-ratio)
number of items get updated with prepends, and a new set of items are added, indicated by the add-count.

- Current design: all operations on buckets - synchronous access
- Specify test cluster-ops information in test.properties
- make compile : To compile the program
- make run : To execute the program
- make clean : To clean up class files

    Setting test.properties:

    nodes...............ip of one node from cluster
    port................couchbase port
    buckets.............list of buckets [format: bucket1:pwd1,bkt2,bkt3:pwd3]
    prefix..............prefix for keys generated
    json................boolean value for json or binary
    item-count..........no. of items to be created
    item-size...........size of the items to be created
    set-ratio...........ratio of sets to gets till item-count reached
    append-ratio........ratio of item-count that will be updated with appends
    append-size.........extra size to be appended to each item
    prepend-ratio.......ratio of item-count that will be updated with prepends
    prepend-size........extra size to be prepended to each item
    del-ratio...........ratio of item-count to be deleted
    exp-ratio...........ratio of items set initially to be set with an expiry time
    expiration..........expiration time for the items set with an expiry time
    add-count...........no. of items to be added extra
    loop................boolean value to loop the load infinitely or not
    check-flag..........boolean value to store the sets to verify a set success
