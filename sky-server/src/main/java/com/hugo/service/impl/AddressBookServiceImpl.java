package com.hugo.service.impl;

import com.hugo.context.BaseContext;
import com.hugo.entity.AddressBook;
import com.hugo.mapper.AddressBookMapper;
import com.hugo.service.AddressBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressBookServiceImpl implements AddressBookService {

    @Autowired
    private AddressBookMapper addressBookMapper;

    @Override
    public void addAddress(AddressBook addressBook) {
        addressBook.setUserId(BaseContext.getCurrentId());
        addressBookMapper.insert(addressBook);
    }

    @Override
    public List<AddressBook> list() {
        return addressBookMapper.list(BaseContext.getCurrentId());
    }

    @Override
    public AddressBook getDefault() {
        AddressBook addressBook = AddressBook.builder()
                .userId(BaseContext.getCurrentId())
                .isDefault(1)
                .build();
        return addressBookMapper.getDefault(addressBook);
    }

    @Override
    public void update(AddressBook addressBook) {
        addressBookMapper.update(addressBook);
    }

    @Override
    public void setDefault(AddressBook addressBook) {
        AddressBook addr = AddressBook.builder()
                .userId(BaseContext.getCurrentId())
                .isDefault(0)
                .build();
        addressBookMapper.updateDefaultByUserId(addr);

        addressBook.setIsDefault(1);
        addressBookMapper.update(addressBook);
    }

    @Override
    public AddressBook getById(Long id) {
        return addressBookMapper.getById(id);
    }

    @Override
    public void delete(Long id) {
        addressBookMapper.delete(id);
    }
}
