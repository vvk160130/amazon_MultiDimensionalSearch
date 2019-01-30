

package vvk160130;

// completed

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.HashSet;
import java.util.Set;

/**
 * Stores items and their corresponding id, price, and description
 *
 * <p>
 * The MDS program implements an application that is able to store
 * items and their corresponding id, price, and descriptions. The
 * application organizes the properties of items in the appropriate
 * data structures to optimize search operations for each search
 * field.
 * </p>
 *
 * <p>
 *  In addition, the following application allows the user to insert new items
 *  and update the information of existing items, find the prices of a specific
 *  item, delete items, find the minimum and maximum prices of a collection of
 *  items, find the number of items that fall under a certain range of prices, and
 *  modifies the descriptions of certain items.
 * </p>
 *
 * @author  Vishnu Kunadharaju
 * NetId    vvk160130
 * @Version 1.0
 * @since   1.0
 */
public class MDS {

    class findAndDelete{

        /**
         * Stores the price of the item being inserted or stores the updated price of the item
         */
        int price;

        /**
         * Stores the description of the item being inserted or the updated description of the item
         */
        List<Integer> currentList = new LinkedList<>();

        /*
         * Sets the price and description of an item to an
         * instance of findAndDelete
         */

        findAndDelete(int cost, List<Integer> list){
            this.price = cost;
            currentList.addAll(list);
        }
    }


    public MDS() {
    }

    /**
     * Stores the item's id as the key, and an instance of findDeleteId class which contain the
     * following properties - price & description list as the value
     */
    HashMap<Integer, findAndDelete> findDeleteId = new HashMap<>();

    /**
     * Stores the description values as keys, and a seperate HashMap as the key's value.
     * Stores the item's id as the key for the inner HashMap and the price as its value.
     */
    HashMap<Integer, HashMap<Integer, Integer>> descriptors = new HashMap<>();


    /**
     * Insert a new item whose description is given in the list
     *
     * <p>
     * If an entry with the same id already exists, then its
     * description and price are replaced by the new values, unless
     * list is null or empty, in which case, just the price is
     * updated
     * </p>
     *
     * @param id        The item's id
     * @param price     The item's price
     * @param list      The item's description
     * @return          int Returns 1 if the item is new, and 0 otherwise
     * Preconditions    Price of item is positive
     * Postconditions   The return value is either a 1 or 0
     */
    public int insert(int id, int price, java.util.List<Integer> list) {

        /*
         * Checks to see if the id already exists in the data structure or not.
         * If it is in the data structure, then the program will perform an update
         * on the item's price and or its description list. Otherwise the program
         * will be inserting a new item to the data structure.
         */

        if(findDeleteId.containsKey(id)){

            /* We are updating the item's price information, since user
             * provided an empty description list for the update.
             */

            if(list.isEmpty()){

                /* We update the price in the 1st data structure holding
                 * the id and findAndDelete instance as the key value pairs
                 */

                findDeleteId.get(id).price = price;

                /*
                 * We iterate through the current id's description elements, and update
                 * the new price for the item for each elements nested HashMap
                 */

                Iterator<Integer> itr = findDeleteId.get(id).currentList.iterator();

                int n = 0;
                while(itr.hasNext()){

                    n = itr.next();

                    HashMap<Integer, Integer> innerMap = descriptors.get(n);
                    innerMap.put(id, price);

                }

            }
            else{

                /*
                 * Here the user has decided to update both the item's price and
                 * its description list. So the program updates the price of the
                 * item in the 1st data structure. Next it loops through all
                 * the elements in the item's description list, and removes
                 * the current id,price pair in the nested HashMap. At the
                 * end we clear the elements that reside in the id's description
                 * list. */

                findDeleteId.get(id).price = price;

                Iterator<Integer> itr = findDeleteId.get(id).currentList.iterator();

                int n = 0;
                while(itr.hasNext()){
                    n = itr.next();
                    descriptors.get(n).remove(id);

                    if(descriptors.get(n).isEmpty()){
                        descriptors.remove(n);
                    }
                }

                findDeleteId.get(id).currentList.clear();


                //------------------------------------------------------------------

                /*
                * Now the program iterates through the list that is passed in
                * (this happens to be the new description for the item). Now
                * in the 2nd data structure, we are updating each descriptors
                * value (a hashmap of id and price as the (key, value) pair )
                * with id and updated price. At the end we are also updating
                * the item's description with the new description passed in.
                */

                itr = list.iterator();

                while(itr.hasNext()){
                    n = itr.next();

                    if(!descriptors.containsKey(n)){

                        descriptors.put(n, new HashMap(){{put(id , price);}});
                    }
                    else{
                        HashMap<Integer, Integer> innerMap = descriptors.get(n);
                        innerMap.put(id, price);
                    }

                    findDeleteId.get(id).currentList.add(n);

                }
            }

            return 0;
        }
        else{

            /*
             * A new element is being inserted into the data structure, and we
             * are not updating the item's price or description list. We create
             * a new node and store the items price and description list. We place
             * this node in the 1st data structure. */

            findAndDelete node = new findAndDelete(price, list);
            findDeleteId.put(id, node);

            /* The program now iterates over the id's description and for each
             * description we access the 2nd data structure, where the descriptor
             * is stored as the key and its value is a hashmap that contains the
             * id, price pair. We add the new id and price to this nested HashMap
             */

            Iterator<Integer> itr = list.iterator();

            int description = 0;
            while(itr.hasNext()){

                description = itr.next();


                if(!descriptors.containsKey(description)){

                    descriptors.put(description, new HashMap(){{put(id , price);}});
                }
                else{
                    HashMap<Integer, Integer> innerMap = descriptors.get(description);
                    innerMap.put(id, price);
                }
            }

            return 1;
        }
    }


    /**
     * Provides price of item with given id
     *
     * @param id        Id of item that is used to provide the appropriate price
     * @return          int Returns price of item with given id (or 0, if not found)
     * Preconditions    Given id of item exists in the data structure (database)
     * Postconditions   Price of given item is positive
     */
    public int find(int id) {

        if(findDeleteId.containsKey(id)){
            return findDeleteId.get(id).price;
        }
        else{
            return 0;
        }
    }

    /**
     * Deletes item from storage
     *
     * @param id        Id of item that is used to delete appropriate item
     * @return          int Returns the sum of the ints that are in the description of the item deleted or 0, if such an id did not exist.
     * Preconditions    Given id of item exists in the data structure (database)
     * Postconditions   The sum of the descriptions is positive
     */
    public int delete(int id) {

        int sum = 0;

        /* If the id of the item is not found in the data structure, then we return 0.
         * Else we proceed to remove the id, description list, and price.
         *
         */

        if(findDeleteId.containsKey(id)){

            /*
            * The id of the item exists in the data structure, and description of the id
            * is temporarily stored in a set
            */

            Set<Integer> remove = new HashSet<>(findDeleteId.get(id).currentList);

            /*
             * We iterate through the id's description, during the iteration of the
             * set, we are calculating the sum of all the elements in the description.
             * This is being handled by the sum variable. In addition to this, we are
             * removing the id and price (key, value) pair of the item being deleted
             * from each descriptor of the list that is being removed.
             */

            Iterator<Integer> itr = remove.iterator();

            int i = 0;


            while(itr.hasNext()){

                i = itr.next();
                sum = sum + i;

                descriptors.get(i).remove(id);

                if(descriptors.get(i).isEmpty()){
                    descriptors.remove(i);
                }
            }


            /*
             * Once the elements in the id's description have been
             * removed, then the id itself is removed from the
             * data structure
             */

            findDeleteId.remove(id);
            return sum;

        }
        else{

            return 0;
        }

    }

    /**
     * Provides the lowest price from 0 or more items
     *
     * <p>
     * Given an integer, it finds items whose description
     * contains that number (exact match with one of
     * the ints in the item's description), and return
     * lowest price of those items.
     * </p>
     *
     * @param n         Descriptor being searched for in the item's description list
     * @return          int Returns the lowest price of a item or (0 if their is no such item)
     * Preconditions    Given descriptor of the item exists in the data structure (database)
     * Postconditions   The price of the item is positive
     */
    public int findMinPrice(int n) {

        /*
        * If the descriptor n is not located in the 2nd data structure then
        * we return 0 as the minimum price
        * */

        if(descriptors.get(n) == null){
            return 0;
        }
        else{

            /*
             * The descriptor n is stored in the data structure (hashMap)
             * as the key. We retrieve its value, a HashMap, that holds
             * the id and price as its key and value. These are all the
             * items that contain the descriptor n in their description
             * list.
             */

            HashMap<Integer, Integer> innerMap = descriptors.get(n);

            /*
             * Iterates through the prices of items which contain
             * the descriptor n. It iterates through each element
             * and performs a comparison to determine the minimum price.
             *
             */

            Iterator<Integer> itr = innerMap.values().iterator();

            int min = -1;
            int i = -1;

            min = itr.next();

            while(itr.hasNext()){

                i = itr.next();

                if(min > i){
                    min = i;
                }
            }


            return min;
        }

    }

    /**
     * Provides the highest price from 0 or more items
     *
     * <p>
     * Given an integer, it finds items whose description
     * contains that number
     * </p>
     *
     * @param n         Descriptor being searched for in the item's description list
     * @return          int Returns the highest price of those items or (0 if their is no such item(
     * Preconditions    Given descriptor of the item exists in the data structure (database)
     * Postconditions   The price of the item is positive
     */
    public int findMaxPrice(int n) {

        /*
         * If descriptor n is not found then we return the max price as 0
         * */

        if(descriptors.get(n) == null){
            return 0;
        }
        else{

            /*
             * The descriptor n is stored in the data structure (hashMap)
             * as the key. We retrieve its value, a HashMap, that holds
             * the id and price as its key and value. These are all the
             * items that contain the descriptor n in their description
             * list.
             */

            HashMap<Integer, Integer> innerMap = descriptors.get(n);

            /*
             * Iterates through the prices of items which contain
             * the descriptor n. It iterates through each element
             * and performs a comparison to determine the max price.
             *
             */

            Iterator<Integer> itr = innerMap.values().iterator();

            int max = -1;
            int i = -1;

            max = itr.next();

            while(itr.hasNext()){
                i = itr.next();

                if(max < i){
                    max = i;
                }
            }

            return max;
        }
    }

    /**
     * Provides the count of the number of items which match the following condition
     *
     * <p>
     * Given int n, it finds the number of items whose description contains n, and in
     * addition, their prices fall within the given range, [low, high]
     * </p>
     *
     * @param n         Descriptor being searched for in the item's description list
     * @param low       Lowest range value that prices are being compared to
     * @param high      Highest range value that prices are being compared to
     * @return          int Returns number of items that match the following conditions
     * Precondition     The given descriptor n of item exists in the data structure (database)
     * Postcondition    The count will be positive
     */
    public int findPriceRange(int n, int low, int high) {

        /*
         * If the descriptor is not found then the count is returned as 0
         */

        if(descriptors.get(n) == null){
            return 0;
        }
        else{

            /*
             * The descriptor n is stored in the data structure (a hashMap)
             * as the key. We retrieve its value, a HashMap, that holds
             * the id and price as its key and value. These are all the
             * items that contain the descriptor n in their description
             * list.
             */

            HashMap<Integer, Integer> innerMap = descriptors.get(n);

            /*
             * Iterates through the prices of items that correspond
             * to descriptor n and increments count for each price
             * that matches the criteria
             */

            Iterator<Integer> itr = innerMap.values().iterator();

            int count = 0;
            int i = 0;

            while(itr.hasNext()){

                i = itr.next();

                if(i >= low && i <= high){
                    count++;
                }
            }

            return count;
        }
    }


    /**
     * Removes elements of list from the description of id.
     *
     * <p>
     * Removes elements of list from the description of id.
     * It is possible that some of the items in the list are
     * not in the id's description.
     * </p>
     *
     * @param id        Item's id, removing elements from this id's description
     * @param list      List of elements that will be removed from description of id
     * @return          int Returns the sum of the numbers that are actually deleted from the description of id or (0 if id does not exist)
     * Precondition     The given id of the item exists in the data structure
     * Postcondition    The sum is a positive value
     */
    public int removeNames(int id, java.util.List<Integer> list) {

        List<Integer> updatedList = new LinkedList<>();

        /*
         * If the id of the item is not located in the 1st data structure then the
         * program returns 0
         *
         */

        if(findDeleteId.get(id) == null){
            return 0;
        }
        else{

            /*
            * If the list passed in is empty or the description list associated
            * with the id is empty, then we return 0 since we will not be deleting
            * any numbers from the description of id.
            */

            if(list.isEmpty() || findDeleteId.get(id).currentList.isEmpty()){
                return 0;
            }
            else{

                /*
                 * We transfer the elements from the description of id to a set.
                 * This deals with the case where their are duplicated elements in
                 * the description list.
                 */

                Set<Integer> descriptorSet = new HashSet<>(findDeleteId.get(id).currentList);

                /*
                * The program iterates through the list that is passed in, if a match
                * is found between the set and element in the list then that element
                * is removed from the description list of id. We total up all the
                * descriptions that were deleted from the description list of id
                * which is handled by the sum variable. Since we are deleting a
                * descriptor, we also delete the id, price pair from its
                * nested hashmap. Then we remove the descriptor that was matched
                * from the set itself.
                */

                Iterator<Integer> itr = list.iterator();

                int i = 0;
                int sum = 0;
                while(itr.hasNext()){

                    i = itr.next();

                    if(descriptorSet.contains(i)){
                        sum = sum + i;

                        descriptors.get(i).remove(id);

                        if(descriptors.get(i).isEmpty()){
                            descriptors.remove(i);
                        }

                        descriptorSet.remove(i);
                    }
                }

                /*
                 * If all the elements from the id's description list have been
                 * deleted, then we update the description of the item with a
                 * empty list. Otherwise we update the id's description with
                 * the remaining elements that were not deleted
                 *
                 * */

                if(descriptorSet.isEmpty()){
                    findDeleteId.get(id).currentList = updatedList;
                }
                else{

                    updatedList.addAll(descriptorSet);
                    findDeleteId.get(id).currentList = updatedList;

                }

                return sum;
            }
        }



    }
}
