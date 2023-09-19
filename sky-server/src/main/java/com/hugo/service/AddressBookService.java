package com.hugo.service;

import com.hugo.entity.AddressBook;

import java.util.List;

public interface AddressBookService {
    void addAddress(AddressBook addressBook);

    List<AddressBook> list();

    AddressBook getDefault();

    void update(AddressBook addressBook);

    void setDefault(AddressBook addressBook);

    AddressBook getById(Long id);

    void delete(Long id);
}
