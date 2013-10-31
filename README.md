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
