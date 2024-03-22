import random
import math

def T1(a):
    return a

def S1(a):
    return T1(a)/math.sqrt(1+(T1(a)**2))

def C1(a):
    return 1/math.sqrt(1+(T1(a)**2))

def T2(a):
    result = (3 * a) / (3 - (a**2))
    return result

def S2(a):
    return T2(a)/math.sqrt(1+(T2(a)**2))

def C2(a):
    return 1/math.sqrt(1+(T2(a)**2))

def T3(a):
    result = (15 * a - (a**3)) / (15 - 6 * (a**2))
    return result

def S3(a):
    return T3(a)/math.sqrt(1+(T3(a)**2))

def C3(a):
    return 1/math.sqrt(1+(T3(a)**2))

def T4(a): # Functia T(4,a)
    result = (105 * a - 10 * (a**3)) / (105 - 45 * (a**2) + (a**4))
    return result

def S4(a):
    return T4(a)/math.sqrt(1+(T3(a)**2))

def C4(a):
    return 1/math.sqrt(1+(T4(a)**2))

def T5(a): 
    result = (945 * a - 105 * (a ** 3) + (a ** 5)) / (945 - 420 * (a**2) + 15 * (a**4))
    return result

def S5(a):
    return T5(a)/math.sqrt(1+(T5(a)**2))

def C5(a):
    return 1/math.sqrt(1+(T5(a)**2))

def T6(a): 
    result = (10395 * a - 1260 * (a ** 3) + 21 * (a ** 5)) / (10395 - 4725 * (a**2) + 210 * (a**4) - (a**6))
    return result

def S6(a):
    return T6(a)/math.sqrt(1+(T6(a)**2))

def C6(a):
    return 1/math.sqrt(1+(T6(a)**2))

def T7(a): 
    result = (135135 * a - 17325 * (a ** 3) + 378 * (a ** 5) - (a**7)) / (135135 - 62370 * (a**2) + 3150 * (a**4) - 28 * (a**6))
    return result

def S7(a):
    return T7(a)/math.sqrt(1+(T7(a)**2))

def C7(a):
    return 1/math.sqrt(1+(T7(a)**2))

def T8(a): 
    result = (2027025 * a - 270270 * (a ** 3) + 6930 * (a ** 5) - 36* (a**7)) / (2027025 - 945945 * (a**2) + 51975 * (a**4) - 630 * (a**6) + (a**8))
    return result

def S8(a):
    return T8(a)/math.sqrt(1+(T8(a)**2))

def C8(a):
    return 1/math.sqrt(1+(T8(a)**2))

def T9(a): 
    result = (34459425 * a - 4729725 * (a ** 3) + 135135 * (a ** 5) - 990 * (a**7) + (a**9)) / (34459425 - 16216200 * (a**2) + 945945 * (a**4) - 13860 * (a**6) + 45 * (a**8))
    return result

def S9(a):
    return T9(a)/math.sqrt(1+(T9(a)**2))

def C9(a):
    return 1/math.sqrt(1+(T9(a)**2))

random.seed(13)

lower_limit = -math.pi/2
upper_limit = math.pi/2

# scor pt fiecare fct => +3 pt locul 1, +2 pt 2, +1 pt locul 3
functionScore = [0] * 10 #initializat cu 0
functions = [S1,S2,S3,S4,S5,S6,S7,S8,S9]
#functions = [C1,C2,C3,C4,C5,C6,C7,C8,C9] #PENTRU COSINUS + SCHIMBA FUNCTIA IN FOR

# Generam 10 000 numere in intervalul dat
for i in range(10000):
   # Alege random un nr
   number = random.uniform(lower_limit, upper_limit)
   print("Pentru numarul",number,"avem functiile:")

   # Calculam valorile celor 6 functii in punctul ales random si le punem intr-o lista
   results = [func(number) for func in functions]

   # calculam val exacta si eroarea pt fiecare functie
   vExact = math.sin(number)
   error = [abs(result - vExact) for result in results]

   #facem perechi de tipul (functia Ti(a) , eroarea functiei)
   tuples = list(enumerate(error))

   # sortam tuplele pe baza erorii - adica pe baza ccelui de-al doilea elem din tuplu (eroarea sa fie cat mai mica => crescator)
   sorted_tuples = sorted(tuples, key=lambda x: x[1])

    # apoi luam primele 3 cu eroarea cea mai mica si incrementam scorul in functie de loc: 
   count = 3
   for i in sorted_tuples[:3]:
    functionScore[i[0]] = functionScore[i[0]] + count #pune 3 pt cele de pe locul 1, 2 pt cele de pe locul 2, 1 pt locul 3
    count -= 1
    nume=i[0]+1 #pt ca lista incepe de la 0
    print("T"+ str(nume))
    

# apoi decidem topul final

# obtinem index-value pairs pt vectorul de scor (fct, scor)
tuples_final = list(enumerate(functionScore))

# sortam descrescator dupa valoare
tuples_final_Sortat = sorted(tuples_final, key=lambda x: x[1], reverse=True)

# afisam topul
print("Topul functiilor:")
for index, value in tuples_final_Sortat[:9]:
    print(f"Functie: {index+1}, Scor: {value}")