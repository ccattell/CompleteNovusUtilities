package me.ccattell.plugins.cnu.commands;

import java.util.HashMap;
import static me.ccattell.plugins.cnu.CompleteNovusUtilities.plugin;
import me.ccattell.plugins.cnu.database.MainResultSet;
import me.ccattell.plugins.cnu.database.QueryFactory;
import me.ccattell.plugins.cnu.utilities.MajorMinor;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author Charlie
 */
public class BankCommand implements CommandExecutor {

    public String prefix = plugin.configs.getBankConfig().getString("Banking.Prefix");
    public boolean BankEnabled = plugin.configs.getBankConfig().getBoolean("Banking.Enabled");
    public String moduleName = ChatColor.GREEN + prefix + ChatColor.RESET + " ";

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (cmd.getName().equalsIgnoreCase("bank")) {
            if(BankEnabled){
                Player player;
                if (sender instanceof Player) {
                    player = (Player) sender;
                } else {
                    sender.sendMessage(moduleName + "The bank command cannot be used from the console!");
                    return true;
                }
                if (!sender.hasPermission("cnu.bank")) {
                    sender.sendMessage(moduleName + "You don't have permission to use the bank!");
                    return true;
                }
                String name = player.getName();
                String transaction_type;
                float transaction_amount;
                float balance;

                if (args.length == 0) {
                    HashMap<String, Object> where_from = new HashMap<String, Object>();
                    where_from.put("player_name", name);
                    MainResultSet fq = new MainResultSet(where_from);
                    if (fq.resultSet()) {
                        balance = fq.getBank();
                        String s = new MajorMinor().getFormat(balance);
                        sender.sendMessage(moduleName + "Your current bank balance is " + s);
                        return true;
                    }
                } else if (args.length == 2) {
                    transaction_type = args[0];
                    try {
                        transaction_amount = Float.parseFloat(args[1]);
                    } catch (NumberFormatException nfe) {
                        return true;
                    }
                    if (transaction_amount > 0) {
                        HashMap<String, Object> where = new HashMap<String, Object>();
                        where.put("player_name", name);
                        MainResultSet fq = new MainResultSet(where);
                        if (fq.resultSet()) {
                            if (transaction_type.equalsIgnoreCase("withdrawal") || transaction_type.equalsIgnoreCase("w")) {
                                balance = fq.getBank();
                                if (balance >= transaction_amount) {
                                    QueryFactory qf = new QueryFactory();
                                    qf.alterBalance("bank", name, -transaction_amount);
                                    qf.alterBalance("cash", name, transaction_amount);
                                    sender.sendMessage(moduleName + "Transaction complete");
                                    return true;
                                } else {
                                    sender.sendMessage(moduleName + "Insufficient funds to complete withdrawal");
                                    return true;
                                }
                            }
                            if (transaction_type.equalsIgnoreCase("deposit") || transaction_type.equalsIgnoreCase("d")) {
                                balance = fq.getCash();
                                if (balance >= transaction_amount) {
                                    QueryFactory qf = new QueryFactory();
                                    qf.alterBalance("bank", name, transaction_amount);
                                    qf.alterBalance("cash", name, -transaction_amount);
                                    sender.sendMessage(moduleName + "Transaction complete");
                                    return true;
                                } else {
                                    sender.sendMessage(moduleName + "Insufficient funds to complete deposit");
                                    return true;
                                }
                            }
                        }
                    }
                } else {
                    sender.sendMessage(moduleName + "Incorrect number of arguments");
                    return true;
                }
            }else{
                sender.sendMessage(moduleName + "Banking has been disabled");
            }
        }
        return true;
    }
}
