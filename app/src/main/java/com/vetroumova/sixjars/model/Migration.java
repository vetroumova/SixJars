package com.vetroumova.sixjars.model;

/**
 * Created by OLGA on 28.10.2016.
 */

import io.realm.DynamicRealm;
import io.realm.RealmMigration;
import io.realm.RealmObjectSchema;
import io.realm.RealmSchema;

/**
 * Example of migrating a Realm file from version 0 (initial version) to its last version (version 3).
 */
public class Migration implements RealmMigration {

    @Override
    public void migrate(final DynamicRealm realm, long oldVersion, long newVersion) {
        // During a migration, a DynamicRealm is exposed.
        // A DynamicRealm is an untyped variant of a normal Realm, but
        // with the same object creation and query capabilities.
        // A DynamicRealm uses Strings instead of Class references because
        // the Classes might not even exist or have been
        // renamed.

        // Access the Realm schema in order to create, modify or delete classes and their fields.
        RealmSchema schema = realm.getSchema();

        /************************************************
         // Version 0
         Class Jar
         @PrimaryKey private String jar_id;
         private String jar_name;
         private String jar_info;
         private float totalCash;
         private float jar_float_id;

         // Version 1
         class Jar
         @PrimaryKey private String jar_id;
         private String jar_name;
         private String jar_info;
         private float totalCash;
         private float jar_float_id;
         private int jar_color;     //new field
         ************************************************/
        // Migrate from version 0 to version 1
        if (oldVersion == 0) {
            /*RealmObjectSchema personSchema = schema.get("Jar");
            // Combine 'firstName' and 'lastName' in a new field called 'fullName'
            personSchema
                    .addField("fullName", String.class, FieldAttribute.REQUIRED)
                    .transform(new RealmObjectSchema.Function() {
                        @Override
                        public void apply(DynamicRealmObject obj) {
                            obj.set("fullName", obj.getString("firstName") + " " + obj.getString("lastName"));
                        }
                    })
                    .removeField("firstName")
                    .removeField("lastName");
            personSchema.addField("jar_color", String.class);
            oldVersion++;*/
            RealmObjectSchema jarSchema = schema.get("Jar");
            // add a new field called 'jar_color'
            if (!jarSchema.hasField("jar_color")) {
                jarSchema.addField("jar_color", int.class);
            }
            oldVersion++;
        }

        /************************************************
         // Version 2
         class Pet                   // add a new model class
         @Required String name;
         @Required String type;
         class Person
         @Required String fullName;
         int age;
         RealmList<Pet> pets;    // add an array property
         ************************************************//*
        // Migrate from version 1 to version 2
        if (oldVersion == 1) {

            // Create a new class
            RealmObjectSchema petSchema = schema.create("Pet")
                    .addField("name", String.class, FieldAttribute.REQUIRED)
                    .addField("type", String.class, FieldAttribute.REQUIRED);

            // Add a new field to an old class and populate it with initial data
            schema.get("Person")
                    .addRealmListField("pets", petSchema)
                    .transform(new RealmObjectSchema.Function() {
                        @Override
                        public void apply(DynamicRealmObject obj) {
                            if (obj.getString("fullName").equals("JP McDonald")) {
                                DynamicRealmObject pet = realm.createObject("Pet");
                                pet.setString("name", "Jimbo");
                                pet.setString("type", "dog");
                                obj.getList("pets").add(pet);
                            }
                        }
                    });
            oldVersion++;
        }

        *//************************************************
         // Version 3
         class Pet
         @Required String name;
         int type;               // type becomes int
         class Person
         String fullName;        // fullName is nullable now
         RealmList<Pet> pets;    // age and pets re-ordered (no action needed)
         int age;
         ************************************************//*
        // Migrate from version 2 to version 3
        if (oldVersion == 2) {
            RealmObjectSchema personSchema = schema.get("Person");
            personSchema.setNullable("fullName", true); // fullName is nullable now.

            // Change type from String to int
            schema.get("Pet")
                    .addField("type_tmp", int.class)
                    .transform(new RealmObjectSchema.Function() {
                        @Override
                        public void apply(DynamicRealmObject obj) {
                            String oldType = obj.getString("type");
                            if (oldType.equals("dog")) {
                                obj.setLong("type_tmp", 1);
                            } else if (oldType.equals("cat")) {
                                obj.setInt("type_tmp", 2);
                            } else if (oldType.equals("hamster")) {
                                obj.setInt("type_tmp", 3);
                            }
                        }
                    })
                    .removeField("type")
                    .renameField("type_tmp", "type");
            oldVersion++;
        }*/
    }
}
