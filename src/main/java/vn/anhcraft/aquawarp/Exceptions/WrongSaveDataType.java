package vn.anhcraft.aquawarp.Exceptions;

/**
 * Copyright (c) by Anh Craft. All rights reserved.
 * Licensed under the apache license v2.0.
 */
public class WrongSaveDataType extends Exception {
    public WrongSaveDataType() {}

    public WrongSaveDataType(String message)
    {
        super(message);
    }
}
