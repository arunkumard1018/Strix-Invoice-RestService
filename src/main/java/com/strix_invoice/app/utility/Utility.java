/**
 * The {@code Utility$} class represents Functionalities
 *
 * @author ArunKumar D
 */

package com.strix_invoice.app.utility;

public class Utility {

    public static String generateInvoiceNumber(String prefix,Integer number){
        return prefix+number;
    }


    public static String generateInvoicePrefix(String businessName) {

        // Remove extra spaces and split the business name by spaces
        String[] words = businessName.trim().split("\\s+");

        // If there's only one word, return the first 3 characters
        if (words.length == 1) {
            return words[0].substring(0, Math.min(3, words[0].length())).toUpperCase();
        }

         /**
          * If there are multiple words,
          * take the first two characters of the first word
          * and the first character of the second word
          * */
        StringBuilder prefix = new StringBuilder();
        prefix.append(words[0].substring(0, Math.min(2, words[0].length()))); // First two letters of the first word

        // Find the first non-empty word after the first one
        for (int i = 1; i < words.length; i++) {
            if (!words[i].isEmpty()) {
                prefix.append(words[i].charAt(0)); // First letter of the next word
                break;
            }
        }
        return prefix.toString().toUpperCase();
    }
}
