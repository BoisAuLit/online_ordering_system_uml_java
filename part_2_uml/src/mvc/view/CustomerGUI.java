package mvc.view;

import java.awt.Color;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import mvc.controller.ConnectionHandler;
import mvc.model.basicTypes.Address;
import mvc.model.basicTypes.Customer;
import mvc.model.basicTypes.Product;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import mvc.controller.StrategyPattern.*;
import mvc.controller.filterPattern.*;
import mvc.model.basicTypes.BoughtItem;
import mvc.model.basicTypes.Cart;
import mvc.model.basicTypes.ItemInCart;
import mvc.model.basicTypes.ItemInOrder;
import mvc.model.basicTypes.Order;
import mvc.model.basicTypes.ShippingMethod;

/**
 *
 * @author bohao
 */
public class CustomerGUI extends javax.swing.JFrame {

	private ArrayList<Product> allProducts;
	private ArrayList<Product> currentProducts;
	private Map productMap;
	private Criteria categoryCriteria;
	private Criteria storeCriteria;
	private Criteria priceCriteria;
	private ProductSorter priceLowToHighSorter;
	private ProductSorter priceHighToLowSorter;
	
	private ArrayList<ItemInCart> itemsInCart;
	private Map itemInCartMap;
	
	private ArrayList<BoughtItem> boughtItems;
	private Map boughtItemMap;
	
	public CustomerGUI() {
//		Customer.display();
		initComponents();
		init();
		setBrwoseCatalogPanel();
		setManageAccountPanel();
		setPlaceOrderPanel();
	}

	private void init() {
		// load the class shipping method
		try {
			Class.forName("mvc.model.basicTypes.ShippingMethod");
		} catch (ClassNotFoundException e) {
			e.printStackTrace(System.out);
			System.exit(0);
		}
		setLocationRelativeTo(null);
		setResizable(false);
		Customer customer = Customer.getInstance();
		if (customer.getCart() != null) {
			int numberOfItems = customer.getCart().getNumberOfItems();
			if (numberOfItems > 1) {
				nbItemInCartLabel.setText(numberOfItems + " items in cart");
			} else {
				nbItemInCartLabel.setText("1 item in cart");
			}
		} else {
			nbItemInCartLabel.setText("0 item in cart");
		}
		
		currentProducts = new ArrayList();
		allProducts = new ArrayList();
		productMap = new HashMap();
		ResultSet rs = ConnectionHandler.getResultSet(
				"SELECT * FROM product");
		try {
			while (rs.next()) {
				allProducts.add(Product.buildProduct(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace(System.out);
			System.exit(0);
		}
		currentProducts.addAll(allProducts);
		allProducts.stream().forEach((product) -> {
			productMap.put(product, new ProductItemPanel(this, product));
		});
		priceLowToHighSorter = new PriceLowToHighSorter();
		priceHighToLowSorter = new PriceHighToLowSorter();
	}
	
	// things to do when closing the customer GUI
	private void closing() {
		dispose();
		Customer.reset();
		new LoginPage().setVisible(true);
	}
	
	private void setBrwoseCatalogPanel() {
		productScrollPane.getVerticalScrollBar().setUnitIncrement(16);
		showAllProducts();
	}

	private void setManageAccountPanel() {
		setBasicInfoPanel();
		setBoughtItemPanel();
	}

	private void setPlaceOrderPanel() {
		cartItemScrollPane.getVerticalScrollBar().setUnitIncrement(16);
		setCartPanel();
		setViewOrderStatusPanel();
	}
	
	private void setCartPanel() {
		Customer customer = Customer.getInstance();
		if (customer.getCart() == null)
			return;
		itemInCartMap = new HashMap();
		itemsInCart = customer.getCart().getItems();
		itemsInCart.stream().forEach((item) -> {
			itemInCartMap.put(item, new CartItemPanel(this, item));
		});
		showAllItemsInCart();
		setCartPrice();
		setNbItemsInCartLabel();
	}
	
	private void addCartItemPanel(ItemInCart itemInCart) {
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = cartItemPanel.getComponentCount();
		gbc.fill = GridBagConstraints.NONE;
		gbc.anchor = GridBagConstraints.PAGE_START;
		gbc.insets = new Insets(2, 2, 2, 2);
		JPanel cartItem = (JPanel) itemInCartMap.get(itemInCart);
		cartItemPanel.add(cartItem, gbc);
		cartItemPanel.revalidate();
		cartItemPanel.invalidate();
		cartItemPanel.repaint();
	}
	
	public void deleteCartItemPanel(ItemInCart itemInCart) {
		Customer.getInstance().getCart().deleteAnItem(itemInCart);
		showAllItemsInCart();
		setCartPrice();
		setNbItemsInCartLabel();
	}
	
	private void setNbItemsInCartLabel() {
		String result;
		if (itemsInCart == null) {
			result = "0 item in cart";
		} else if (itemsInCart.size() == 1) {
			result = "1 item in cart";
		} else {
			result = itemsInCart.size() + " items in cart";
		}
		nbItemInCartLabel.setText(result);
	}
	
	private void setCartPrice() {
		float price = .0f;
		for (ItemInCart item : itemsInCart)
			price += item.getPriceOfProducts();
		cartPriceLabel.setText(String.format("GBP  %.2f", price));
	}
	
	private void showAllItemsInCart() {
		cartItemPanel.removeAll();
		cartItemPanel.repaint();
		itemsInCart.stream().forEach((item) -> {
			addCartItemPanel(item);
		});
	}
	
	private void showAllProducts() {
		allProducts.stream().forEach((product) -> {
			addProductItemPanel(product);
		});
		numberOfItemsLabel.setText(allProducts.size() + " items found");
	}
	
	public void showDetailedInfo(Product product) {
		detailedInfoScrollPane.getVerticalScrollBar().setUnitIncrement(16);
		productTabbedPane.setSelectedIndex(1);
		detailedInfoPanel.removeAll();
		JPanel detailedInfo = new ProductDetailedInfoPanel(this, product);
		detailedInfoPanel.add(detailedInfo);
	}
	
	
	private void addProductItemPanel(Product product) {
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = productPanel.getComponentCount();
		gbc.fill = GridBagConstraints.NONE;
		gbc.anchor = GridBagConstraints.PAGE_START;
		gbc.insets = new Insets(2, 2, 2, 2);
		JPanel productItemPanel = (JPanel)productMap.get(product);
		productPanel.add(productItemPanel, gbc);
		productPanel.revalidate();
		productPanel.invalidate();
		productPanel.repaint();
	}
	
	private void setBasicInfoPanel() {
		addressTextArea.setEditable(false);
		addressTextArea.setBackground(new Color(242,241,240, 1));
		Customer customer = Customer.getInstance();
		userNameValueLabel.setText(customer.getUserName());
		firstNameValueLabel.setText(customer.getFirstName());
		lastNameValueLabel.setText(customer.getLastName());
		emailValueLabel.setText(customer.getEmailAddress());
		phoneValueLabel.setText(customer.getPhoneNumber());
		if (customer.getDefaultAddress() != null) {
			addressTextArea.setText("\tDefault address:\n"
					+ customer.getDefaultAddress().toString());
		} else { // if no default address, there can't be any other addresses
			return;
		}
		ArrayList<Address> otherAddresses = customer.getOtherAddresses();
		if (otherAddresses != null)
			otherAddresses.stream().forEach((address) -> {
				addressTextArea.append("\n\tAnother address:\n"
						+ address.toString());
		});
	}
	
	private void setBoughtItemPanel() {
		// if the customer doesn't have bought items, we do nothing
		if (!Customer.getInstance().has_bought_items()) {
			return;
		}

		boughtItemsScrollPane.getVerticalScrollBar().setUnitIncrement(16);
		
		boughtItems = Customer.getInstance().getBoughtItems();
		boughtItemMap = new HashMap<BoughtItem, BoughtItemPanel>();
		for (BoughtItem boughtItem : boughtItems)
			boughtItemMap.put(boughtItem, new BoughtItemPanel(boughtItem));
		
		// add the BoughtItemPanels into the GUI
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.fill = GridBagConstraints.NONE;
		gbc.anchor = GridBagConstraints.PAGE_START;
		gbc.insets = new Insets(2, 2, 2, 2);
		for (BoughtItem boughtItem : boughtItems) {
			gbc.gridy = boughtItemBigPanel.getComponentCount();
			JPanel boughtItemSmallPanel = (JPanel) boughtItemMap.get(boughtItem);
			boughtItemBigPanel.add(boughtItemSmallPanel, gbc);
		}
	}
	
	private void setViewOrderStatusPanel() {
		Customer customer = Customer.getInstance();
		Order order = customer.getOrder();
		if (order == null)
			return;
		if (!order.getStatus().equalsIgnoreCase("confirmed_by_user"))
			return;
		
		orderStatusScrollPane.getVerticalScrollBar().setUnitIncrement(16);
		
		orderStatusLabel.setText("confirmed by user");
		fullPriceLabel.setText(order.getPriceInTotal() + "  GBP");
		
		ArrayList<ItemInOrder> itemsInOrder = order.getItems();
		ArrayList<JPanel> list = new ArrayList();
		for (ItemInOrder item : itemsInOrder)
			list.add(new WaitingOrderItemPanel(item));
		
		// add the items in the order to the GUI
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.fill = GridBagConstraints.NONE;
		gbc.anchor = GridBagConstraints.PAGE_START;
		gbc.insets = new Insets(2, 2, 2, 2);
		for (JPanel jpanel : list) {
			gbc.gridy = orderStatusPanel.getComponentCount();
			orderStatusPanel.add(jpanel, gbc);
		}

	}
	
	private void createCategoryCriteria() {
		categoryCriteria = new CategoryCriteria();
		if (beverageRadioButton.isSelected())
			((CategoryCriteria)categoryCriteria).setCategory("Beverages");
		else if (breadRadioButton.isSelected())
			((CategoryCriteria)categoryCriteria).setCategory("Bread/Bakery");
		else if (cannedRadioButton.isSelected())
			((CategoryCriteria)categoryCriteria).setCategory(
					"Canned/Jarred Goods");
		else if (dairyRadioButton.isSelected())
			((CategoryCriteria)categoryCriteria).setCategory("Dairy");
		else if (dryRadioButton.isSelected())
			((CategoryCriteria)categoryCriteria).setCategory(
					"Dry/Baking Goods");
		else if (frozonRadioButton.isSelected())
			((CategoryCriteria)categoryCriteria).setCategory("Frozen Goods");
		else if (meatRadioButton.isSelected())
			((CategoryCriteria)categoryCriteria).setCategory("Meat");
		else if (produceRadioButton.isSelected())
			((CategoryCriteria)categoryCriteria).setCategory("Produce");
		else
			((CategoryCriteria)categoryCriteria).setCategory("");
	}
	
	private void createStoreCriteria() {
		storeCriteria = new StoreCriteria();
		if (bohaoButton.isSelected())
			((StoreCriteria)storeCriteria).setStore("Bohao's store");
		else if (antonyButton.isSelected())
			((StoreCriteria)storeCriteria).setStore("Antony's store");
		else if (thomasButton.isSelected())
			((StoreCriteria)storeCriteria).setStore("Thomas's store");
		else
			((StoreCriteria)storeCriteria).setStore("");
	}
	
	private boolean createPriceCriteria() {
		CheaperCriteria cheaperCriteria = new CheaperCriteria();
		MoreExpensiveCriteria moreExpensiveCriteria
				= new MoreExpensiveCriteria();
		String from = fromTextField.getText();
		String to = toTextField.getText();
		float fromPrice = .0f;
		float toPrice = .0f;
		try {
			if (from.isEmpty()) {
				moreExpensiveCriteria.setLimit(-1);
			} else {
				fromPrice = Float.valueOf(from);
				if (fromPrice < 0) {
					JOptionPane.showMessageDialog(null,
							"low limit must be positive",
							"Error", JOptionPane.ERROR_MESSAGE);
					return false;
				}
				moreExpensiveCriteria.setLimit(fromPrice);
			}
			
			if (to.isEmpty()) {
				cheaperCriteria.setLimit(-1);
			} else {
				toPrice = Float.valueOf(to);
				if (toPrice < 0) {
					JOptionPane.showMessageDialog(null,
							"high limit must be positive",
							"Error", JOptionPane.ERROR_MESSAGE);
					return false;
				}
				cheaperCriteria.setLimit(toPrice);
			}
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(null,
					"incorrect number format",
					"Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		
		priceCriteria = new AndCriteria(cheaperCriteria, moreExpensiveCriteria);
		if (!from.isEmpty() && !to.isEmpty() && fromPrice > toPrice) {
			JOptionPane.showMessageDialog(null,
					"low limit can't be high than high limit",
					"Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}

		return true;
	}
	
	private void filterAndShow() {
		if (createPriceCriteria() == false)
			return;
		createCategoryCriteria();
		createStoreCriteria();
		
		AndCriteria finalCriteria = new AndCriteria(categoryCriteria,
				new AndCriteria(storeCriteria, priceCriteria));
		List<Product> filteredProducts = 
				finalCriteria.meetCriteria(allProducts);
		productPanel.removeAll();
		
		currentProducts.clear();
		currentProducts.addAll(filteredProducts);
		
		if (orderComboBox.getSelectedIndex() == 1) {
			priceLowToHighSorter.sort(currentProducts);
		} else if (orderComboBox.getSelectedIndex() == 2) {
			priceHighToLowSorter.sort(currentProducts);
		}
		showCurrentProducts();
	}
	
	private void resetCriteriaPanel() {
		fromTextField.setText("");
		toTextField.setText("");
		noneStoreButton.setSelected(true);
		noneCategoryButton.setSelected(true);
	}
	
	private void showCurrentProducts() {
		productPanel.removeAll();
		currentProducts.stream().forEach((product) -> {
			addProductItemPanel(product);
		});
		numberOfItemsLabel.setText(currentProducts.size() + " items found");
	}
	
	public void itemQuantityChanged(ItemInCart itemInCart) {
		this.setCartPrice();
		((JPanel)itemInCartMap.get(itemInCart)).repaint();
	}
	
	public void addToCart(Product product) {
		if (Customer.getInstance().getCart() == null) {
			Customer.getInstance().setCart(Cart.getInstance());
		}
		Customer.getInstance().getCart().addAnItem(product);

		setCartPanel();
		
//		itemsInCart.add(item);
//		itemInCartMap.put(item, new CartItemPanel(this, item));
		showAllItemsInCart();
		setCartPrice();
		setNbItemsInCartLabel();
	}
	
	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        mainTabbedPane = new javax.swing.JTabbedPane();
        browseCatalogMainPanel = new javax.swing.JPanel();
        searchPanel = new javax.swing.JPanel();
        searchTextField = new javax.swing.JTextField();
        searchButton = new javax.swing.JButton();
        logoutButton = new javax.swing.JButton();
        homeImageLabel = new javax.swing.JLabel();
        cartImageLabel = new javax.swing.JLabel();
        criteriaPanel = new javax.swing.JPanel();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator2 = new javax.swing.JSeparator();
        chooseCategoryLabel = new javax.swing.JLabel();
        beverageRadioButton = new javax.swing.JRadioButton();
        breadRadioButton = new javax.swing.JRadioButton();
        cannedRadioButton = new javax.swing.JRadioButton();
        dairyRadioButton = new javax.swing.JRadioButton();
        meatRadioButton = new javax.swing.JRadioButton();
        dryRadioButton = new javax.swing.JRadioButton();
        frozonRadioButton = new javax.swing.JRadioButton();
        produceRadioButton = new javax.swing.JRadioButton();
        noneCategoryButton = new javax.swing.JRadioButton();
        chooseStoreLabel = new javax.swing.JLabel();
        bohaoButton = new javax.swing.JRadioButton();
        antonyButton = new javax.swing.JRadioButton();
        thomasButton = new javax.swing.JRadioButton();
        choosePriceLabel = new javax.swing.JLabel();
        fromLabel = new javax.swing.JLabel();
        fromTextField = new javax.swing.JTextField();
        toLabel = new javax.swing.JLabel();
        gbpLabel2 = new javax.swing.JLabel();
        toTextField = new javax.swing.JTextField();
        gbpLabel1 = new javax.swing.JLabel();
        categoryValidateButton = new javax.swing.JButton();
        storeValidateButton = new javax.swing.JButton();
        priceValidateButton = new javax.swing.JButton();
        noneStoreButton = new javax.swing.JRadioButton();
        jSeparator3 = new javax.swing.JSeparator();
        resetPriceButton = new javax.swing.JButton();
        productTabbedPane = new javax.swing.JTabbedPane();
        productBigPanel = new javax.swing.JPanel();
        orderPanel = new javax.swing.JPanel();
        orderComboBox = new javax.swing.JComboBox<>();
        sortByLabel = new javax.swing.JLabel();
        nbItemInCartLabel = new javax.swing.JLabel();
        numberOfItemsLabel = new javax.swing.JLabel();
        productScrollPane = new javax.swing.JScrollPane();
        productPanel = new javax.swing.JPanel();
        detailedInfoScrollPane = new javax.swing.JScrollPane();
        detailedInfoPanel = new javax.swing.JPanel();
        manageAccountTabbedPane = new javax.swing.JTabbedPane();
        basicInfoPanel = new javax.swing.JPanel();
        emailValueLabel = new javax.swing.JLabel();
        firstNameLabel = new javax.swing.JLabel();
        lastNameLabel = new javax.swing.JLabel();
        addressLabel = new javax.swing.JLabel();
        emailLabel = new javax.swing.JLabel();
        phoneLabel = new javax.swing.JLabel();
        userNameLabel = new javax.swing.JLabel();
        userNameValueLabel = new javax.swing.JLabel();
        firstNameValueLabel = new javax.swing.JLabel();
        phoneValueLabel = new javax.swing.JLabel();
        lastNameValueLabel = new javax.swing.JLabel();
        detailedInfoScroll = new javax.swing.JScrollPane();
        addressTextArea = new javax.swing.JTextArea();
        boughtItemsScrollPane = new javax.swing.JScrollPane();
        boughtItemBigPanel = new javax.swing.JPanel();
        jPanel13 = new javax.swing.JPanel();
        orderStatusScrollPane = new javax.swing.JScrollPane();
        orderStatusPanel = new javax.swing.JPanel();
        subTotalPanel2 = new javax.swing.JPanel();
        orderStatusLabel = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        fullPriceLabel = new javax.swing.JLabel();
        orderConfirmTabbedPane = new javax.swing.JTabbedPane();
        cartPanel = new javax.swing.JPanel();
        cartItemScrollPane = new javax.swing.JScrollPane();
        cartItemPanel = new javax.swing.JPanel();
        subTotalPanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        cartPriceLabel = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        orderConfirmPanel = new javax.swing.JPanel();
        subTotalPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        cartPriceLabel1 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        confirmOrderScrollPane = new javax.swing.JScrollPane();
        confirmOrderPanel = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        mainTabbedPane.setFont(new java.awt.Font("Arvo", 1, 15)); // NOI18N

        browseCatalogMainPanel.setBackground(new java.awt.Color(70, 237, 217));
        browseCatalogMainPanel.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        searchPanel.setBackground(new java.awt.Color(135, 223, 56));

        searchTextField.setFont(new java.awt.Font("Ubuntu", 0, 24)); // NOI18N
        searchTextField.setForeground(new java.awt.Color(92, 216, 239));

        searchButton.setFont(new java.awt.Font("Ubuntu Light", 1, 24)); // NOI18N
        searchButton.setText("Search");
        searchButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchButtonActionPerformed(evt);
            }
        });

        logoutButton.setText("Logout");
        logoutButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                logoutButtonActionPerformed(evt);
            }
        });

        homeImageLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/image/icon/home.png"))); // NOI18N
        homeImageLabel.setBorder(null);
        homeImageLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                homeImageLabelMouseClicked(evt);
            }
        });

        cartImageLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/image/icon/cart.png"))); // NOI18N
        cartImageLabel.setBorder(null);
        cartImageLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cartImageLabelMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout searchPanelLayout = new javax.swing.GroupLayout(searchPanel);
        searchPanel.setLayout(searchPanelLayout);
        searchPanelLayout.setHorizontalGroup(
            searchPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(searchPanelLayout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addComponent(homeImageLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(cartImageLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(65, 65, 65)
                .addComponent(searchTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 353, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31)
                .addComponent(searchButton, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(66, 66, 66)
                .addComponent(logoutButton)
                .addGap(25, 25, 25))
        );
        searchPanelLayout.setVerticalGroup(
            searchPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(searchPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(searchPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(searchPanelLayout.createSequentialGroup()
                        .addGroup(searchPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(searchPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(searchTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(searchButton, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(searchPanelLayout.createSequentialGroup()
                                .addGap(31, 31, 31)
                                .addComponent(logoutButton)))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(homeImageLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cartImageLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        criteriaPanel.setBackground(new java.awt.Color(238, 241, 134));

        jSeparator1.setBackground(new java.awt.Color(25, 21, 17));
        jSeparator1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jSeparator2.setBackground(new java.awt.Color(25, 21, 17));
        jSeparator2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        chooseCategoryLabel.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        chooseCategoryLabel.setText("Choose category");

        beverageRadioButton.setBackground(new java.awt.Color(238, 241, 134));
        buttonGroup1.add(beverageRadioButton);
        beverageRadioButton.setText("Beverage");

        breadRadioButton.setBackground(new java.awt.Color(238, 241, 134));
        buttonGroup1.add(breadRadioButton);
        breadRadioButton.setText("Bread/Bakery");

        cannedRadioButton.setBackground(new java.awt.Color(238, 241, 134));
        buttonGroup1.add(cannedRadioButton);
        cannedRadioButton.setText("Canned/Jarred Goods");

        dairyRadioButton.setBackground(new java.awt.Color(238, 241, 134));
        buttonGroup1.add(dairyRadioButton);
        dairyRadioButton.setText("Dairy");

        meatRadioButton.setBackground(new java.awt.Color(238, 241, 134));
        buttonGroup1.add(meatRadioButton);
        meatRadioButton.setText("Meat");

        dryRadioButton.setBackground(new java.awt.Color(238, 241, 134));
        buttonGroup1.add(dryRadioButton);
        dryRadioButton.setText("Dry/Baking goods");

        frozonRadioButton.setBackground(new java.awt.Color(238, 241, 134));
        buttonGroup1.add(frozonRadioButton);
        frozonRadioButton.setText("Frozon foods");

        produceRadioButton.setBackground(new java.awt.Color(238, 241, 134));
        buttonGroup1.add(produceRadioButton);
        produceRadioButton.setText("Produce");

        noneCategoryButton.setBackground(new java.awt.Color(238, 241, 134));
        buttonGroup1.add(noneCategoryButton);
        noneCategoryButton.setSelected(true);
        noneCategoryButton.setText("None");

        chooseStoreLabel.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        chooseStoreLabel.setText("Choose store");

        bohaoButton.setBackground(new java.awt.Color(238, 241, 134));
        buttonGroup2.add(bohaoButton);
        bohaoButton.setText("Bohao's store");

        antonyButton.setBackground(new java.awt.Color(238, 241, 134));
        buttonGroup2.add(antonyButton);
        antonyButton.setText("Antony's store");

        thomasButton.setBackground(new java.awt.Color(238, 241, 134));
        buttonGroup2.add(thomasButton);
        thomasButton.setText("Thomas's store");

        choosePriceLabel.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        choosePriceLabel.setText("Choose price");

        fromLabel.setText("From");

        fromTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fromTextFieldActionPerformed(evt);
            }
        });

        toLabel.setText("To");

        gbpLabel2.setText("GBP");

        gbpLabel1.setText("GBP");

        categoryValidateButton.setText(">>");
        categoryValidateButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                categoryValidateButtonActionPerformed(evt);
            }
        });

        storeValidateButton.setText(">>");
        storeValidateButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                storeValidateButtonActionPerformed(evt);
            }
        });

        priceValidateButton.setText(">>");
        priceValidateButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                priceValidateButtonActionPerformed(evt);
            }
        });

        noneStoreButton.setBackground(new java.awt.Color(238, 241, 134));
        buttonGroup2.add(noneStoreButton);
        noneStoreButton.setSelected(true);
        noneStoreButton.setText("None");

        jSeparator3.setBackground(new java.awt.Color(28, 21, 14));
        jSeparator3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        resetPriceButton.setText("Reset");
        resetPriceButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resetPriceButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout criteriaPanelLayout = new javax.swing.GroupLayout(criteriaPanel);
        criteriaPanel.setLayout(criteriaPanelLayout);
        criteriaPanelLayout.setHorizontalGroup(
            criteriaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator2)
            .addComponent(jSeparator3, javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(criteriaPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(criteriaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(criteriaPanelLayout.createSequentialGroup()
                        .addComponent(noneCategoryButton, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSeparator1, javax.swing.GroupLayout.DEFAULT_SIZE, 19, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(categoryValidateButton))
                    .addComponent(dryRadioButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(criteriaPanelLayout.createSequentialGroup()
                        .addComponent(noneStoreButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(storeValidateButton))
                    .addGroup(criteriaPanelLayout.createSequentialGroup()
                        .addGap(48, 48, 48)
                        .addComponent(resetPriceButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(priceValidateButton))
                    .addGroup(criteriaPanelLayout.createSequentialGroup()
                        .addGroup(criteriaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(criteriaPanelLayout.createSequentialGroup()
                                .addGroup(criteriaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, criteriaPanelLayout.createSequentialGroup()
                                        .addComponent(toLabel)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(toTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, criteriaPanelLayout.createSequentialGroup()
                                        .addComponent(fromLabel)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(fromTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(criteriaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(gbpLabel1)
                                    .addComponent(gbpLabel2)))
                            .addComponent(beverageRadioButton)
                            .addComponent(cannedRadioButton)
                            .addComponent(dairyRadioButton)
                            .addComponent(meatRadioButton)
                            .addComponent(frozonRadioButton)
                            .addComponent(breadRadioButton)
                            .addComponent(produceRadioButton)
                            .addComponent(chooseCategoryLabel)
                            .addComponent(choosePriceLabel)
                            .addComponent(bohaoButton)
                            .addComponent(antonyButton)
                            .addComponent(thomasButton)
                            .addComponent(chooseStoreLabel))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        criteriaPanelLayout.setVerticalGroup(
            criteriaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(criteriaPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(chooseCategoryLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(beverageRadioButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(breadRadioButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cannedRadioButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(dairyRadioButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(dryRadioButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(frozonRadioButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(meatRadioButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(produceRadioButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(criteriaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(criteriaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(categoryValidateButton)
                        .addComponent(noneCategoryButton))
                    .addGroup(criteriaPanelLayout.createSequentialGroup()
                        .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(19, 19, 19)))
                .addGap(13, 13, 13)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(chooseStoreLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bohaoButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(antonyButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(thomasButton)
                .addGroup(criteriaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(criteriaPanelLayout.createSequentialGroup()
                        .addGap(9, 9, 9)
                        .addComponent(storeValidateButton))
                    .addGroup(criteriaPanelLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(noneStoreButton)))
                .addGap(18, 18, 18)
                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(choosePriceLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(criteriaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(fromLabel)
                    .addComponent(fromTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(gbpLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(criteriaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(toLabel)
                    .addComponent(toTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(gbpLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(criteriaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(resetPriceButton)
                    .addComponent(priceValidateButton))
                .addContainerGap())
        );

        orderPanel.setBackground(new java.awt.Color(237, 183, 227));

        orderComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "None", "Price: Low to High", "Price: High to Low" }));
        orderComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                orderComboBoxActionPerformed(evt);
            }
        });

        sortByLabel.setFont(new java.awt.Font("Ubuntu Light", 1, 15)); // NOI18N
        sortByLabel.setText("Sort by");

        nbItemInCartLabel.setFont(new java.awt.Font("Ubuntu Light", 1, 18)); // NOI18N

        numberOfItemsLabel.setFont(new java.awt.Font("Ubuntu", 1, 18)); // NOI18N

        javax.swing.GroupLayout orderPanelLayout = new javax.swing.GroupLayout(orderPanel);
        orderPanel.setLayout(orderPanelLayout);
        orderPanelLayout.setHorizontalGroup(
            orderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, orderPanelLayout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(nbItemInCartLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(93, 93, 93)
                .addComponent(numberOfItemsLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 155, Short.MAX_VALUE)
                .addComponent(sortByLabel)
                .addGap(27, 27, 27)
                .addComponent(orderComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        orderPanelLayout.setVerticalGroup(
            orderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(orderPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(orderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(numberOfItemsLabel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(orderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(sortByLabel)
                        .addComponent(orderComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(nbItemInCartLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 20, Short.MAX_VALUE))
        );

        productScrollPane.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        productPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        productPanel.setLayout(new java.awt.GridBagLayout());
        productScrollPane.setViewportView(productPanel);

        javax.swing.GroupLayout productBigPanelLayout = new javax.swing.GroupLayout(productBigPanel);
        productBigPanel.setLayout(productBigPanelLayout);
        productBigPanelLayout.setHorizontalGroup(
            productBigPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(productScrollPane)
            .addComponent(orderPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        productBigPanelLayout.setVerticalGroup(
            productBigPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(productBigPanelLayout.createSequentialGroup()
                .addComponent(orderPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(productScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 655, Short.MAX_VALUE))
        );

        productTabbedPane.addTab("Products information", productBigPanel);

        detailedInfoPanel.setLayout(new java.awt.GridLayout(1, 0));
        detailedInfoScrollPane.setViewportView(detailedInfoPanel);

        productTabbedPane.addTab("Detailed information", detailedInfoScrollPane);

        javax.swing.GroupLayout browseCatalogMainPanelLayout = new javax.swing.GroupLayout(browseCatalogMainPanel);
        browseCatalogMainPanel.setLayout(browseCatalogMainPanelLayout);
        browseCatalogMainPanelLayout.setHorizontalGroup(
            browseCatalogMainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(searchPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(browseCatalogMainPanelLayout.createSequentialGroup()
                .addComponent(criteriaPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(productTabbedPane))
        );
        browseCatalogMainPanelLayout.setVerticalGroup(
            browseCatalogMainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(browseCatalogMainPanelLayout.createSequentialGroup()
                .addComponent(searchPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(7, 7, 7)
                .addGroup(browseCatalogMainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(criteriaPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(productTabbedPane)))
        );

        mainTabbedPane.addTab("Browse catalog", browseCatalogMainPanel);

        manageAccountTabbedPane.setFont(new java.awt.Font("Arvo", 1, 15)); // NOI18N

        emailValueLabel.setFont(new java.awt.Font("Ubuntu", 0, 24)); // NOI18N
        emailValueLabel.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        firstNameLabel.setFont(new java.awt.Font("Ubuntu", 0, 28)); // NOI18N
        firstNameLabel.setText("First name:");

        lastNameLabel.setFont(new java.awt.Font("Ubuntu", 0, 28)); // NOI18N
        lastNameLabel.setText("Last name:");

        addressLabel.setFont(new java.awt.Font("Ubuntu", 0, 28)); // NOI18N
        addressLabel.setText("Address:");

        emailLabel.setFont(new java.awt.Font("Ubuntu", 0, 28)); // NOI18N
        emailLabel.setText("Email address:");

        phoneLabel.setFont(new java.awt.Font("Ubuntu", 0, 28)); // NOI18N
        phoneLabel.setText("Phone number:");

        userNameLabel.setFont(new java.awt.Font("Ubuntu", 0, 28)); // NOI18N
        userNameLabel.setText("User name: ");

        userNameValueLabel.setFont(new java.awt.Font("Ubuntu", 0, 24)); // NOI18N
        userNameValueLabel.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        firstNameValueLabel.setFont(new java.awt.Font("Ubuntu", 0, 24)); // NOI18N
        firstNameValueLabel.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        phoneValueLabel.setFont(new java.awt.Font("Ubuntu", 0, 24)); // NOI18N
        phoneValueLabel.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        lastNameValueLabel.setFont(new java.awt.Font("Ubuntu", 0, 24)); // NOI18N
        lastNameValueLabel.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        addressTextArea.setColumns(20);
        addressTextArea.setRows(5);
        detailedInfoScroll.setViewportView(addressTextArea);

        javax.swing.GroupLayout basicInfoPanelLayout = new javax.swing.GroupLayout(basicInfoPanel);
        basicInfoPanel.setLayout(basicInfoPanelLayout);
        basicInfoPanelLayout.setHorizontalGroup(
            basicInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(basicInfoPanelLayout.createSequentialGroup()
                .addGap(97, 97, 97)
                .addGroup(basicInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(firstNameLabel)
                    .addComponent(lastNameLabel)
                    .addComponent(userNameLabel)
                    .addComponent(emailLabel)
                    .addComponent(phoneLabel)
                    .addComponent(addressLabel))
                .addGap(51, 51, 51)
                .addGroup(basicInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(phoneValueLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 505, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(emailValueLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 505, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(userNameValueLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 505, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(firstNameValueLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 505, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lastNameValueLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 505, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(detailedInfoScroll, javax.swing.GroupLayout.PREFERRED_SIZE, 537, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(228, Short.MAX_VALUE))
        );
        basicInfoPanelLayout.setVerticalGroup(
            basicInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(basicInfoPanelLayout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addGroup(basicInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(userNameLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 48, Short.MAX_VALUE)
                    .addComponent(userNameValueLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(basicInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(firstNameValueLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(firstNameLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 48, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(basicInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lastNameLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lastNameValueLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(basicInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(basicInfoPanelLayout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addComponent(emailValueLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(basicInfoPanelLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(emailLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 112, Short.MAX_VALUE)
                .addGroup(basicInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(phoneValueLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(phoneLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 48, Short.MAX_VALUE))
                .addGap(35, 35, 35)
                .addGroup(basicInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(detailedInfoScroll, javax.swing.GroupLayout.PREFERRED_SIZE, 271, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(addressLabel))
                .addGap(72, 72, 72))
        );

        manageAccountTabbedPane.addTab("View basic info", basicInfoPanel);

        boughtItemBigPanel.setLayout(new java.awt.GridBagLayout());
        boughtItemsScrollPane.setViewportView(boughtItemBigPanel);

        manageAccountTabbedPane.addTab("View bought items", boughtItemsScrollPane);

        orderStatusPanel.setLayout(new java.awt.GridBagLayout());
        orderStatusScrollPane.setViewportView(orderStatusPanel);

        subTotalPanel2.setBackground(new java.awt.Color(238, 241, 106));
        subTotalPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 3));

        orderStatusLabel.setFont(new java.awt.Font("Ubuntu", 1, 24)); // NOI18N
        orderStatusLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        orderStatusLabel.setBorder(null);

        jLabel6.setFont(new java.awt.Font("Ubuntu", 1, 24)); // NOI18N
        jLabel6.setText("order status:");

        jLabel7.setFont(new java.awt.Font("Ubuntu", 1, 24)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(230, 17, 17));
        jLabel7.setText("Full price");

        fullPriceLabel.setFont(new java.awt.Font("Ubuntu", 1, 24)); // NOI18N
        fullPriceLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        fullPriceLabel.setBorder(null);

        javax.swing.GroupLayout subTotalPanel2Layout = new javax.swing.GroupLayout(subTotalPanel2);
        subTotalPanel2.setLayout(subTotalPanel2Layout);
        subTotalPanel2Layout.setHorizontalGroup(
            subTotalPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(subTotalPanel2Layout.createSequentialGroup()
                .addGroup(subTotalPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(subTotalPanel2Layout.createSequentialGroup()
                        .addGap(92, 92, 92)
                        .addComponent(jLabel6))
                    .addGroup(subTotalPanel2Layout.createSequentialGroup()
                        .addGap(51, 51, 51)
                        .addGroup(subTotalPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(fullPriceLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(orderStatusLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(subTotalPanel2Layout.createSequentialGroup()
                        .addGap(92, 92, 92)
                        .addComponent(jLabel7)))
                .addContainerGap(53, Short.MAX_VALUE))
        );
        subTotalPanel2Layout.setVerticalGroup(
            subTotalPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(subTotalPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6)
                .addGap(18, 18, 18)
                .addComponent(orderStatusLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(fullPriceLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addComponent(orderStatusScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 721, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 29, Short.MAX_VALUE)
                .addComponent(subTotalPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(orderStatusScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 813, Short.MAX_VALUE)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addGap(71, 71, 71)
                .addComponent(subTotalPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        manageAccountTabbedPane.addTab("View order status", jPanel13);

        mainTabbedPane.addTab("Manage account", manageAccountTabbedPane);

        orderConfirmTabbedPane.setFont(new java.awt.Font("Arvo", 1, 15)); // NOI18N

        cartItemPanel.setInheritsPopupMenu(true);
        cartItemPanel.setLayout(new java.awt.GridBagLayout());
        cartItemScrollPane.setViewportView(cartItemPanel);

        subTotalPanel.setBackground(new java.awt.Color(238, 241, 106));
        subTotalPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 3));

        jLabel1.setFont(new java.awt.Font("Ubuntu", 1, 24)); // NOI18N
        jLabel1.setText("Subtotal:");

        cartPriceLabel.setFont(new java.awt.Font("Ubuntu", 1, 24)); // NOI18N
        cartPriceLabel.setForeground(new java.awt.Color(213, 26, 26));
        cartPriceLabel.setText("GBP 0");

        jButton1.setFont(new java.awt.Font("Ubuntu", 1, 18)); // NOI18N
        jButton1.setText("Go to order confirm page");

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/image/icon/cart_2.png"))); // NOI18N

        javax.swing.GroupLayout subTotalPanelLayout = new javax.swing.GroupLayout(subTotalPanel);
        subTotalPanel.setLayout(subTotalPanelLayout);
        subTotalPanelLayout.setHorizontalGroup(
            subTotalPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(subTotalPanelLayout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, subTotalPanelLayout.createSequentialGroup()
                .addGroup(subTotalPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(subTotalPanelLayout.createSequentialGroup()
                        .addGap(51, 51, 51)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(cartPriceLabel))
                    .addGroup(subTotalPanelLayout.createSequentialGroup()
                        .addContainerGap(38, Short.MAX_VALUE)
                        .addComponent(jButton1)))
                .addGap(67, 67, 67))
        );
        subTotalPanelLayout.setVerticalGroup(
            subTotalPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(subTotalPanelLayout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addGroup(subTotalPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, subTotalPanelLayout.createSequentialGroup()
                        .addComponent(cartPriceLabel)
                        .addGap(26, 26, 26)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 26, Short.MAX_VALUE)
                .addComponent(jButton1)
                .addGap(47, 47, 47))
        );

        javax.swing.GroupLayout cartPanelLayout = new javax.swing.GroupLayout(cartPanel);
        cartPanel.setLayout(cartPanelLayout);
        cartPanelLayout.setHorizontalGroup(
            cartPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cartPanelLayout.createSequentialGroup()
                .addComponent(cartItemScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 729, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 30, Short.MAX_VALUE)
                .addComponent(subTotalPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        cartPanelLayout.setVerticalGroup(
            cartPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(cartItemScrollPane)
            .addGroup(cartPanelLayout.createSequentialGroup()
                .addGap(56, 56, 56)
                .addComponent(subTotalPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(504, Short.MAX_VALUE))
        );

        orderConfirmTabbedPane.addTab("View shopping list", cartPanel);

        orderConfirmPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        subTotalPanel1.setBackground(new java.awt.Color(238, 241, 106));
        subTotalPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 3));

        jLabel2.setFont(new java.awt.Font("Ubuntu", 1, 24)); // NOI18N
        jLabel2.setText("Subtotal:");

        cartPriceLabel1.setFont(new java.awt.Font("Ubuntu", 1, 24)); // NOI18N
        cartPriceLabel1.setForeground(new java.awt.Color(213, 26, 26));
        cartPriceLabel1.setText("GBP 0");

        jButton2.setFont(new java.awt.Font("Ubuntu", 1, 18)); // NOI18N
        jButton2.setText("Confirm order");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/image/icon/buy.png"))); // NOI18N

        javax.swing.GroupLayout subTotalPanel1Layout = new javax.swing.GroupLayout(subTotalPanel1);
        subTotalPanel1.setLayout(subTotalPanel1Layout);
        subTotalPanel1Layout.setHorizontalGroup(
            subTotalPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(subTotalPanel1Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(jLabel2)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, subTotalPanel1Layout.createSequentialGroup()
                .addGap(51, 51, 51)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 68, Short.MAX_VALUE)
                .addComponent(cartPriceLabel1)
                .addGap(67, 67, 67))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, subTotalPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton2)
                .addGap(86, 86, 86))
        );
        subTotalPanel1Layout.setVerticalGroup(
            subTotalPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(subTotalPanel1Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(jLabel2)
                .addGap(18, 18, 18)
                .addGroup(subTotalPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, subTotalPanel1Layout.createSequentialGroup()
                        .addComponent(cartPriceLabel1)
                        .addGap(26, 26, 26)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 26, Short.MAX_VALUE)
                .addComponent(jButton2)
                .addGap(47, 47, 47))
        );

        orderConfirmPanel.add(subTotalPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(753, 56, 325, -1));

        confirmOrderPanel.setInheritsPopupMenu(true);
        confirmOrderPanel.setLayout(new java.awt.GridBagLayout());
        confirmOrderScrollPane.setViewportView(confirmOrderPanel);

        orderConfirmPanel.add(confirmOrderScrollPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 735, 813));

        orderConfirmTabbedPane.addTab("Order confirm page", orderConfirmPanel);

        mainTabbedPane.addTab("Place order", orderConfirmTabbedPane);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(mainTabbedPane)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(mainTabbedPane)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void fromTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fromTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_fromTextFieldActionPerformed

    private void logoutButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_logoutButtonActionPerformed
		closing();
    }//GEN-LAST:event_logoutButtonActionPerformed

    private void cartImageLabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cartImageLabelMouseClicked
        mainTabbedPane.setSelectedIndex(2);
		orderConfirmTabbedPane.setSelectedIndex(0);
    }//GEN-LAST:event_cartImageLabelMouseClicked

    private void categoryValidateButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_categoryValidateButtonActionPerformed
		filterAndShow();
    }//GEN-LAST:event_categoryValidateButtonActionPerformed

    private void storeValidateButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_storeValidateButtonActionPerformed
        filterAndShow();
    }//GEN-LAST:event_storeValidateButtonActionPerformed

    private void priceValidateButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_priceValidateButtonActionPerformed
        filterAndShow();
    }//GEN-LAST:event_priceValidateButtonActionPerformed

    private void resetPriceButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resetPriceButtonActionPerformed
        resetCriteriaPanel();
    }//GEN-LAST:event_resetPriceButtonActionPerformed

    private void orderComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_orderComboBoxActionPerformed
        if (orderComboBox.getSelectedIndex() == 1) {
			priceLowToHighSorter.sort(currentProducts);
			productPanel.removeAll();
			showCurrentProducts();
		} else if (orderComboBox.getSelectedIndex() == 2) {
			priceHighToLowSorter.sort(currentProducts);
			productPanel.removeAll();
			showCurrentProducts();
		}
    }//GEN-LAST:event_orderComboBoxActionPerformed

    private void homeImageLabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_homeImageLabelMouseClicked
		resetCriteriaPanel();
		currentProducts.clear();
		currentProducts.addAll(allProducts);
		searchTextField.setText("");
		orderComboBox.setSelectedItem("None");
		productTabbedPane.setSelectedIndex(0);
		productPanel.removeAll();
		showAllProducts();
    }//GEN-LAST:event_homeImageLabelMouseClicked

    private void searchButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchButtonActionPerformed
        resetCriteriaPanel();
		String searchText = searchTextField.getText();
		if (searchText.isEmpty())
			return;
		List<Product> match = new ArrayList();
		allProducts.stream().filter((product) -> (product.getName().toLowerCase()
				.contains(searchText.toLowerCase()))).forEach((product) -> {
					match.add(product);
		});
		
		currentProducts.clear();
		currentProducts.addAll(match);
		showCurrentProducts();
    }//GEN-LAST:event_searchButtonActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton2ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel addressLabel;
    private javax.swing.JTextArea addressTextArea;
    private javax.swing.JRadioButton antonyButton;
    private javax.swing.JPanel basicInfoPanel;
    private javax.swing.JRadioButton beverageRadioButton;
    private javax.swing.JRadioButton bohaoButton;
    private javax.swing.JPanel boughtItemBigPanel;
    private javax.swing.JScrollPane boughtItemsScrollPane;
    private javax.swing.JRadioButton breadRadioButton;
    private javax.swing.JPanel browseCatalogMainPanel;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.JRadioButton cannedRadioButton;
    private javax.swing.JLabel cartImageLabel;
    private javax.swing.JPanel cartItemPanel;
    private javax.swing.JScrollPane cartItemScrollPane;
    private javax.swing.JPanel cartPanel;
    private javax.swing.JLabel cartPriceLabel;
    private javax.swing.JLabel cartPriceLabel1;
    private javax.swing.JButton categoryValidateButton;
    private javax.swing.JLabel chooseCategoryLabel;
    private javax.swing.JLabel choosePriceLabel;
    private javax.swing.JLabel chooseStoreLabel;
    private javax.swing.JPanel confirmOrderPanel;
    private javax.swing.JScrollPane confirmOrderScrollPane;
    private javax.swing.JPanel criteriaPanel;
    private javax.swing.JRadioButton dairyRadioButton;
    private javax.swing.JPanel detailedInfoPanel;
    private javax.swing.JScrollPane detailedInfoScroll;
    private javax.swing.JScrollPane detailedInfoScrollPane;
    private javax.swing.JRadioButton dryRadioButton;
    private javax.swing.JLabel emailLabel;
    private javax.swing.JLabel emailValueLabel;
    private javax.swing.JLabel firstNameLabel;
    private javax.swing.JLabel firstNameValueLabel;
    private javax.swing.JLabel fromLabel;
    private javax.swing.JTextField fromTextField;
    private javax.swing.JRadioButton frozonRadioButton;
    private javax.swing.JLabel fullPriceLabel;
    private javax.swing.JLabel gbpLabel1;
    private javax.swing.JLabel gbpLabel2;
    private javax.swing.JLabel homeImageLabel;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JLabel lastNameLabel;
    private javax.swing.JLabel lastNameValueLabel;
    private javax.swing.JButton logoutButton;
    private javax.swing.JTabbedPane mainTabbedPane;
    private javax.swing.JTabbedPane manageAccountTabbedPane;
    private javax.swing.JRadioButton meatRadioButton;
    private javax.swing.JLabel nbItemInCartLabel;
    private javax.swing.JRadioButton noneCategoryButton;
    private javax.swing.JRadioButton noneStoreButton;
    private javax.swing.JLabel numberOfItemsLabel;
    private javax.swing.JComboBox<String> orderComboBox;
    private javax.swing.JPanel orderConfirmPanel;
    private javax.swing.JTabbedPane orderConfirmTabbedPane;
    private javax.swing.JPanel orderPanel;
    private javax.swing.JLabel orderStatusLabel;
    private javax.swing.JPanel orderStatusPanel;
    private javax.swing.JScrollPane orderStatusScrollPane;
    private javax.swing.JLabel phoneLabel;
    private javax.swing.JLabel phoneValueLabel;
    private javax.swing.JButton priceValidateButton;
    private javax.swing.JRadioButton produceRadioButton;
    private javax.swing.JPanel productBigPanel;
    private javax.swing.JPanel productPanel;
    private javax.swing.JScrollPane productScrollPane;
    private javax.swing.JTabbedPane productTabbedPane;
    private javax.swing.JButton resetPriceButton;
    private javax.swing.JButton searchButton;
    private javax.swing.JPanel searchPanel;
    private javax.swing.JTextField searchTextField;
    private javax.swing.JLabel sortByLabel;
    private javax.swing.JButton storeValidateButton;
    private javax.swing.JPanel subTotalPanel;
    private javax.swing.JPanel subTotalPanel1;
    private javax.swing.JPanel subTotalPanel2;
    private javax.swing.JRadioButton thomasButton;
    private javax.swing.JLabel toLabel;
    private javax.swing.JTextField toTextField;
    private javax.swing.JLabel userNameLabel;
    private javax.swing.JLabel userNameValueLabel;
    // End of variables declaration//GEN-END:variables

	public JTabbedPane getTabbedPane() {
		return productTabbedPane;
	}
}
