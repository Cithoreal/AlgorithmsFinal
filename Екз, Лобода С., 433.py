import tkinter as tk
from tkinter import simpledialog, messagebox
import random
from datetime import datetime

#Функція для визначення переможця гри.
def determine_winner(player_choice, computer_choice):
    if player_choice == computer_choice:
        return "Нічия!"
    elif (player_choice == "Камінь" and computer_choice == "Ножиці") or \
         (player_choice == "Ножиці" and computer_choice == "Папір") or \
         (player_choice == "Папір" and computer_choice == "Камінь"):
        return "Ви виграли!"
    else:
        return "Комп'ютер виграв!"

#Функція для оновлення рахунку та збереження результатів у файл.
def update_scores(result):
    global player_score, computer_score
    if result == "Ви виграли!":
        player_score += 1
    elif result == "Комп'ютер виграв!":
        computer_score += 1
    player_score_label.config(text=f"Гравець: {player_score}")
    computer_score_label.config(text=f"Комп'ютер: {computer_score}")
    save_results(result)

#Функція для збереження результатів у файл з відміткою про час.
def save_results(result):
    timestamp = datetime.now().strftime("%Y-%m-%d %H:%M:%S")
    with open("game_results.txt", "a") as file:
        file.write(f"{timestamp} - Результат: {result}\n")

#Функція для виконання гри.
def play():
    choices = ["Камінь", "Ножиці", "Папір"]
    computer_choice = random.choice(choices)
    player_choice = player_choice_var.get()
    result = determine_winner(player_choice, computer_choice)
    update_scores(result)
    result_label.config(text=f"Комп'ютер вибрав: {computer_choice}\n{result}")

#Функція для початку нової гри.
def new_game():
    global player_score, computer_score
    player_score = 0
    computer_score = 0
    player_score_label.config(text=f"Гравець: {player_score}")
    computer_score_label.config(text=f"Комп'ютер: {computer_score}")
    result_label.config(text="")
    messagebox.showinfo("Нова гра", "Нова гра розпочата!")

# Функція для зміни назви гри.
def change_game_name():
    new_game_name = simpledialog.askstring("Зміна назви гри", "Введіть нову назву гри:")
    if new_game_name:
        root.title(new_game_name)

# Створення головного вікна
root = tk.Tk()
root.title("Гра Камінь-Ножиці-Папір")
root.geometry("300x250")  
root.config(bg="#ECECEC")  

player_score = 0
computer_score = 0

player_choice_var = tk.StringVar()
player_choice_var.set("Камінь")

button_style = {'width': 10, 'height': 2, 'font': ('Arial', 12)}

choices = ["Камінь", "Ножиці", "Папір"]
choice_menu = tk.OptionMenu(root, player_choice_var, *choices)
choice_menu.config(**button_style, bg='#87CEEB')
choice_menu.pack(pady=10)

play_button = tk.Button(root, text="Грати!", command=play, **button_style, bg='#4CAF50', fg='white')
play_button.pack()

menu_bar = tk.Menu(root)
root.config(menu=menu_bar)
new_game_menu = tk.Menu(menu_bar, tearoff=0)
menu_bar.add_cascade(label="Гра", menu=new_game_menu)
new_game_menu.add_command(label="Нова гра", command=new_game)
new_game_menu.add_command(label="Змінити назву гри", command=change_game_name)

player_score_label = tk.Label(root, text=f"Гравець: {player_score}", font=('Arial', 12), fg='blue')
player_score_label.pack()
computer_score_label = tk.Label(root, text=f"Комп'ютер: {computer_score}", font=('Arial', 12), fg='red')
computer_score_label.pack()

result_label = tk.Label(root, text="", font=('Arial', 12))
result_label.pack()

# Запуск циклу програми
root.mainloop()


