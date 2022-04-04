package at.fhv.teame.application.impl;

import at.fhv.teame.domain.model.invoice.Invoice;
import at.fhv.teame.domain.repositories.InvoiceRepository;
import at.fhv.teame.infrastructure.HibernateInvoiceRepository;
import at.fhv.teame.sharedlib.dto.InvoiceDTO;
import at.fhv.teame.sharedlib.rmi.SearchInvoiceService;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class SearchInvoiceServiceImpl extends UnicastRemoteObject implements SearchInvoiceService {
    private final InvoiceRepository invoiceRepository = new HibernateInvoiceRepository();

    public SearchInvoiceServiceImpl() throws RemoteException {
        super();
    }

    @Override
    public InvoiceDTO invoiceById(String s) throws RemoteException {
        // TODO: implement this method
        return null;
    }

    private InvoiceDTO buildInvoiceDTO(Invoice invoice) {
        // TODO: implement this method, needed by invoiceById
        return null;
    }
}
