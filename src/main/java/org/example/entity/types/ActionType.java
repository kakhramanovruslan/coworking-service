package org.example.entity.types;

/**
 * Enumeration representing different actions that can be performed in the system.
 * Each action type corresponds to a specific operation or functionality.
 */
public enum ActionType {

    /**
     * Represents the action of user registration.
     */
    REGISTRATION,

    /**
     * Represents the action of user authorization.
     */
    AUTHORIZATION,

    /**
     * Represents the action of booking workspace.
     */
    BOOK_WORKSPACE,

    /**
     * Represents the action of creating workspace.
     */
    CREATE_WORKSPACE,

    /**
     * Represents the action of deleting workspace.
     */
    DELETE_WORKSPACE,

    /**
     * Represents the action of updating workspace.
     */
    UPDATE_WORKSPACE, LOGOUT,
}