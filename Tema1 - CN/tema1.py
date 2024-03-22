print("******************************************************************** EX1:")
def ex1():
    u = 1.0
    while (1.0 + u != 1.0):
        aux = u
        u = u / 10.0

    return aux

precizia_masina = ex1()
print(f"Precizia masinii este: {precizia_masina}")


print("******************************************************************** EX2:")
# Exemplu neasociativitate adunare
u = 1e-15
x = 1.0
y = u / 10.0
z = u / 10.0

#Pentru adunare
parteaStangaEc1 = (x + y) + z
print(f"    parteaStangaEc1 = {parteaStangaEc1}")
parteaDreaptaEc1 = x + (y + z)
print(f"    parteaDreaptaEc1 = {parteaDreaptaEc1}")
 
if (parteaStangaEc1 != parteaDreaptaEc1):
    raspunsAdunare = True
else:
    raspunsAdunare = False

print(f"Adunarea nu este asociativa pentru numerele date: {raspunsAdunare}")

# Exemplu neasociativitate inmultire
a = 2.5
b = 1.4567
c = 47.88888


#Pentru inmultire
parteaStangaEc2 = (a * b) * c 
print(f"    parteaStangaEc2 = {parteaStangaEc2}")
parteaDreaptaEc2 = a * (b * c)
print(f"    parteaDreaptaEc2 = {parteaDreaptaEc2}")

if (parteaStangaEc2 != parteaDreaptaEc2):
    raspunsInmultire = True
else:
    raspunsInmultire = False
print(f"Inmultirea nu este asociativa pentru numerele date: {raspunsInmultire}")

print("******************************************************************** EX3:")
import random
import math

def T1(a):
    return a

def T2(a):
    result = (3 * a) / (3 - (a**2))
    return result

def T3(a):
    result = (15 * a - (a**3)) / (15 - 6 * (a**2))
    return result

def T4(a): # Functia T(4,a)
    result = (105 * a - 10 * (a**3)) / (105 - 45 * (a**2) + (a**4))
    return result

def T5(a): 
    result = (945 * a - 105 * (a ** 3) + (a ** 5)) / (945 - 420 * (a**2) + 15 * (a**4))
    return result

def T6(a): 
    result = (10395 * a - 1260 * (a ** 3) + 21 * (a ** 5)) / (10395 - 4725 * (a**2) + 210 * (a**4) - (a**6))
    return result

def T7(a): 
    result = (135135 * a - 17325 * (a ** 3) + 378 * (a ** 5) - (a**7)) / (135135 - 62370 * (a**2) + 3150 * (a**4) - 28 * (a**6))
    return result

def T8(a): 
    result = (2027025 * a - 270270 * (a ** 3) + 6930 * (a ** 5) - 36* (a**7)) / (2027025 - 945945 * (a**2) + 51975 * (a**4) - 630 * (a**6) + (a**8))
    return result

def T9(a): 
    result = (34459425 * a - 4729725 * (a ** 3) + 135135 * (a ** 5) - 990 * (a**7) + (a**9)) / (34459425 - 16216200 * (a**2) + 945945 * (a**4) - 13860 * (a**6) + 45 * (a**8))
    return result


random.seed(13)

lower_limit = -math.pi/2
upper_limit = math.pi/2

# scor pt fiecare fct => +3 pt locul 1, +2 pt 2, +1 pt locul 3
functionScore = [0] * 10 #initializat cu 0
functions = [T1,T2,T3,T4,T5,T6,T7,T8,T9]

# Generam 10 000 numere in intervalul dat
for i in range(10000):
   # Alege random un nr
   number = random.uniform(lower_limit, upper_limit)
   print("Pentru numarul",number,"avem functiile:") #..........................

   # Calculam valorile celor 6 functii in punctul ales random si le punem intr-o lista
   results = [func(number) for func in functions]

   # calculam val exacta si eroarea pt fiecare functie
   vExact = math.tan(number)
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
    print("T"+ str(nume))  #..........................
    

# apoi decidem topul final

# obtinem index-value pairs pt vectorul de scor (fct, scor)
tuples_final = list(enumerate(functionScore))

# sortam descrescator dupa valoare
tuples_final_Sortat = sorted(tuples_final, key=lambda x: x[1], reverse=True)

# afisam topul
print("Topul functiilor:")
for index, value in tuples_final_Sortat[:9]:
    print(f"Functia: {index+1}, Scor: {value}")




   
   

