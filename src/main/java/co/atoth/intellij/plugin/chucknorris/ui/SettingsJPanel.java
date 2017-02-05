package co.atoth.intellij.plugin.chucknorris.ui;

import com.intellij.util.ImageLoader;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.text.NumberFormat;

import static co.atoth.intellij.plugin.chucknorris.settings.PluginSettings.*;

public class SettingsJPanel extends JPanel {
	
	private static final long serialVersionUID = 1L;
	
	private JTextField textFieldFirstName;
	private JTextField textFieldLastName;
	private JFormattedTextField textFieldFactNo;

	public SettingsJPanel() {
	    setOpaque(false);

		setBorder(new EmptyBorder(5, 5, 5, 5));
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 0.0, 1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		JLabel lblFactSource = new JLabel("Fact source:");
		GridBagConstraints gbc_lblFactSource = new GridBagConstraints();
		gbc_lblFactSource.anchor = GridBagConstraints.EAST;
		gbc_lblFactSource.insets = new Insets(0, 0, 5, 5);
		gbc_lblFactSource.gridx = 0;
		gbc_lblFactSource.gridy = 0;
		add(lblFactSource, gbc_lblFactSource);
		
		//Currently hardcoded until functionality implemented
		ComboBoxModel<String> comboBoxModel = new DefaultComboBoxModel<>(new String[]{"The Internet Chuck Norris Database (icndb.com)"});
		JComboBox<String> comboBoxFactSource = new JComboBox<String>(comboBoxModel);
		comboBoxFactSource.setToolTipText("//TODO");
		comboBoxFactSource.setEnabled(false);
		
		GridBagConstraints gbc_comboBoxFactSource = new GridBagConstraints();
		gbc_comboBoxFactSource.insets = new Insets(0, 0, 5, 5);
		gbc_comboBoxFactSource.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBoxFactSource.gridx = 1;
		gbc_comboBoxFactSource.gridy = 0;
		add(comboBoxFactSource, gbc_comboBoxFactSource);
		
		JLabel lblLoadedFactsNo = new JLabel("No. of facts loaded:");
		GridBagConstraints gbc_lblLoadedFactsNo = new GridBagConstraints();
		gbc_lblLoadedFactsNo.anchor = GridBagConstraints.EAST;
		gbc_lblLoadedFactsNo.insets = new Insets(0, 0, 5, 5);
		gbc_lblLoadedFactsNo.gridx = 0;
		gbc_lblLoadedFactsNo.gridy = 1;
		add(lblLoadedFactsNo, gbc_lblLoadedFactsNo);
		
		
		NumberFormat format = NumberFormat.getInstance();
	    NumberFormatter formatter = new NumberFormatter(format);
	    formatter.setValueClass(Integer.class);
	    formatter.setMinimum(1);
	    formatter.setMaximum(101);
	    formatter.setAllowsInvalid(false);
	    // If you want the value to be committed on each keystroke instead of focus lost
	    formatter.setCommitsOnValidEdit(true);
	    textFieldFactNo = new JFormattedTextField(formatter);
		GridBagConstraints gbc_textFieldFactNo = new GridBagConstraints();
		gbc_textFieldFactNo.insets = new Insets(0, 0, 5, 5);
		gbc_textFieldFactNo.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFieldFactNo.gridx = 1;
		gbc_textFieldFactNo.gridy = 1;
		add(textFieldFactNo, gbc_textFieldFactNo);
		textFieldFactNo.setColumns(10);
		
		JSeparator separator = new JSeparator();
		GridBagConstraints gbc_separator = new GridBagConstraints();
		gbc_separator.fill = GridBagConstraints.HORIZONTAL;
		gbc_separator.gridwidth = 3;
		gbc_separator.gridx = 0;
		gbc_separator.gridy = 2;
		add(separator, gbc_separator);
		
		JLabel lblCustomNameIn = new JLabel("Custom name in facts:");
		GridBagConstraints gbc_lblCustomNameIn = new GridBagConstraints();
		gbc_lblCustomNameIn.anchor = GridBagConstraints.WEST;
		gbc_lblCustomNameIn.gridwidth = 2;
		gbc_lblCustomNameIn.insets = new Insets(5, 0, 5, 5);
		gbc_lblCustomNameIn.gridx = 0;
		gbc_lblCustomNameIn.gridy = 3;
		add(lblCustomNameIn, gbc_lblCustomNameIn);
		
		JPanel panel = new JPanel();
		panel.setOpaque(false);
		FlowLayout flowLayout = (FlowLayout) panel.getLayout();
		flowLayout.setHgap(0);
		flowLayout.setVgap(0);
		flowLayout.setAlignment(FlowLayout.LEFT);
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.insets = new Insets(0, 0, 0, 5);
		gbc_panel.gridwidth = 3;
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridx = 0;
		gbc_panel.gridy = 4;
		add(panel, gbc_panel);
		
		JPanel panelFirstName = new JPanel();
		panelFirstName.setOpaque(false);
		panel.add(panelFirstName);
		
		JLabel lblFirstName = new JLabel("First name:");
		panelFirstName.add(lblFirstName);
		
		textFieldFirstName = new JTextField();
		textFieldFirstName.setText(DEFAULT_FIRST_NAME);
		panelFirstName.add(textFieldFirstName);
		textFieldFirstName.setColumns(10);
		
		JPanel panelLastName = new JPanel();
		panelLastName.setOpaque(false);
		panel.add(panelLastName);
		
		JLabel lblLastName = new JLabel("Last name:");
		panelLastName.add(lblLastName);
		
		textFieldLastName = new JTextField();
		textFieldLastName.setText(DEFAULT_LAST_NAME);
		panelLastName.add(textFieldLastName);
		textFieldLastName.setColumns(10);
	}
	
	public String getFirstName(){
		return textFieldFirstName.getText().trim();
	}
	
	public String getLastName(){
		return textFieldLastName.getText().trim();
	}
	
	public int getFactNo(){
		try{
            return (int)textFieldFactNo.getValue();
		} catch (Exception e) {
			return DEFAULT_FACT_NO;
		}	
	}
	
	public void setFirstName(String firstName){
		textFieldFirstName.setText(firstName.trim());
	}
	
	public void setLastName(String lastName){
		textFieldLastName.setText(lastName.trim());
	}
	
	public void setFactNo(int factNo){
		textFieldFactNo.setValue(factNo);
	}

    @Override
    protected void paintComponent(Graphics g) {
        Image img = ImageLoader.loadFromResource("/img/chuck.png");
        int x = (int) (g.getClipBounds().getWidth() - img.getWidth(null));
        int y = (int) (g.getClipBounds().getHeight() - img.getHeight(null));
        //magic (img would flicker on the top of the panel at the start, for some reason)
        if(x > 50 && y > 50) {
            g.drawImage(img, x, y, null);
        }
        super.paintComponent(g);
    }

}