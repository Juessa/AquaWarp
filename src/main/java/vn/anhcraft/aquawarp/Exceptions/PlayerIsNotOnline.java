package vn.anhcraft.aquawarp.Exceptions;

/**
 * Copyright (c) by Anh Craft. All rights reserved.
 * Licensed under the apache license v2.0.
 */
public class PlayerIsNotOnline extends Exception {
    public PlayerIsNotOnline() {}

    public PlayerIsNotOnline(String message)
    {
        super(message);
    }
}
