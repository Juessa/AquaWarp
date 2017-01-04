package vn.anhcraft.aquawarp.Exceptions;

/**
 * Copyright (c) by Anh Craft. All rights reserved.
 * Licensed under the apache license v2.0.
 */
public class LackMoney extends Exception {
    public LackMoney() {}

    public LackMoney(String message)
    {
        super(message);
    }
}